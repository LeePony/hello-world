package com.ztesoft.zsmart.report.printqry.bll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.service.DynamicDict;
import com.ztesoft.zsmart.core.threadpool.GeneralThreadPool;
import com.ztesoft.zsmart.core.threadpool.ThreadPoolFactory;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmart.report.printqry.bll.notify.ListenerRegistry;
import com.ztesoft.zsmart.report.printqry.bll.notify.PdfExporter;
import com.ztesoft.zsmart.report.printqry.bll.notify.RtfExporter;
import com.ztesoft.zsmart.report.printqry.bll.notify.XlsExpoter;
import com.ztesoft.zsmart.report.printqry.bll.thread.GenerateBillThread;
import com.ztesoft.zsmart.report.printqry.cfg.BillGenerateCfg;

public class BillGenerateCtrl extends BaseBillCtrl {
	private static final ZSmartLogger _logger = ZSmartLogger.getLogger(BillGenerateCtrl.class);
    private String isSysIn;
	public static Map threadArrMap = new HashMap();

	public Integer succCount = new Integer(0);
	
	private LogFileTimer logTimer  = null;
	public  GeneralThreadPool gte  = null;	
		
	public  final static  String BILL_GENERATE_THREAD_POOL_NAME = "BatchBillGenerate";
    /**
     * 构造函数
     * @param isSysIn <br>
     */
    public BillGenerateCtrl(String isSysIn) {
        this.isSysIn = isSysIn;
    }
	public void doWork(String billFilePath) throws Exception
	{
        if (!preDo(billFilePath , this.isSysIn)) {
            return;
        }
		//读取配置
                
         BillGenerateCfg.logBillGenerateCfg();
         BillGenerateCfg.printBillGenerateCfg();
         addTaskToRegistry();
         DynamicDict dt = new DynamicDict();
		 dt.set("FILE_PATH_LIST", billFileArrayList);		 
		 generateBill(dt);
	}
	/**
	 * 增加任务
	 *
	 */
	private void addTaskToRegistry()
	{
		String[] taskList = BillGenerateCfg.EXPORT_TASK.split(",");
        if(taskList==null||taskList.length==0)
        {
        	taskList = new String[1];
        	taskList[0] = "pdfexport";        	
        }
        for(int i=0;i<taskList.length;i++)
        {
        	if(taskList[i].equalsIgnoreCase("pdfexport"))
        	{
        		PdfExporter exp = new PdfExporter();
        		ListenerRegistry.getDefault().addExecutable(exp);
        		Reporter.println("Pdf export task has be added.");
        	}
        	if(taskList[i].equalsIgnoreCase("rtfexport"))
        	{
        		RtfExporter exp = new RtfExporter();
        		ListenerRegistry.getDefault().addExecutable(exp);
        		Reporter.println("Rtf export task has be added.");
        	}
        	if(taskList[i].equalsIgnoreCase("xlsexport"))
        	{
        		XlsExpoter exp = new XlsExpoter();
        		ListenerRegistry.getDefault().addExecutable(exp);
        		Reporter.println("Xls export task has be added.");
        	}
        }       
        
	}	
	/**
	 * 批量生成帐单
	 * 
	 * @param dict
	 * @throws BaseAppException
	 */
	private void generateBill(DynamicDict dict) throws BaseAppException {
		ArrayList al = (ArrayList) dict.getList("FILE_PATH_LIST");
		GenerateBillThread generateBillThread = null;
		//System.out.println("begin to generate:"+new java.util.Date().toLocaleString());
			if (al != null) {
				
			    if(!ThreadPoolFactory.checkThreadPoolExist(BillGenerateCtrl.BILL_GENERATE_THREAD_POOL_NAME))
			    {
			    	gte = ThreadPoolFactory.createGeneralThreadPool("BatchBillGenerate", BillGenerateCfg.THREAD_POOL_CORE_SIZE);
			    }
			    
			    generateBillThread = 	new GenerateBillThread(gte);
				generateBillThread.setDaemon(true);
				generateBillThread.logFileTimer = logTimer;			

			
				for (int i = 0; i < al.size(); i++) {
					DynamicDict bo = (DynamicDict) al.get(i);
					//System.out.println(bo.asXML("UTF-8"));
					String path = bo.getString("FILE_PATH");	

					generateBillThread.billDataFilePathList.add(path);						
				}

				generateBillThread.start();
			}			
			try
			{
				Thread.sleep(2000);
				boolean endFlag = false;
				while(!endFlag)
				{					
					if(generateBillThread.getParsedXmlFileCnt()==al.size())
					{
						endFlag = true;
						Reporter.printlnWithTime("Export bill file end ");
						gte.shutDown();
						break;
					}
					Thread.sleep(2000);					
				}
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
				_logger.error(e);
			}		
	}	
}

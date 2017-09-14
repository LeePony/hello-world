package com.ztesoft.zsmart.bss.selfcare.servlet;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.coobird.thumbnailator.Thumbnails;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import com.ztesoft.zsmart.bss.common.config.bll.ConfigItemCache;
import com.ztesoft.zsmart.bss.selfcare.bll.ThumbnailConvert;
import com.ztesoft.zsmart.bss.selfcare.model.SessionDto;
import com.ztesoft.zsmart.core.configuation.ConfigurationMgr;
import com.ztesoft.zsmart.core.exception.BaseAppException;
import com.ztesoft.zsmart.core.utils.StringUtil;
import com.ztesoft.zsmart.core.utils.ZSmartLogger;
import com.ztesoft.zsmart.web.resource.Common;

/**
 * <Description> <br>
 * 
 * @author zhang.liang<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016年9月20日 <br>
 * @since V8.0<br>
 * @see com.ztesoft.zsmart.bss.selfcare.servlet <br>
 */
public class FileUploadServlet extends HttpServlet {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * logger
     */
    private static final ZSmartLogger logger = ZSmartLogger.getLogger(FileUploadServlet.class);

    /**
     * LOCAL_CHARSET <br>
     */
    private static final String LOCAL_CHARSET = "UTF-8";

    /**
     * Constructor of the object.
     */
    public FileUploadServlet() {
        super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    public void destroy() {
        super.destroy();
    }

    /**
     * The doPost method of the servlet. <br>
     * This method is called when a form has its tag value method equals to post.
     * 
     * @param request the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        upload(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        upload(request, response);
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param request <br>
     * @param response <br>
     * @throws ServletException <br>
     * @throws IOException <br>
     */
    @SuppressWarnings("unchecked")
    private void upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(LOCAL_CHARSET);
        response.setContentType("text/plain; charset=" + LOCAL_CHARSET);
        PrintWriter writer = response.getWriter();

        JSONArray json = new JSONArray();

        // 上传文件
        String errCodeGlobal = null;
        String fileSizeMax = ConfigItemCache.instance().getString("CUSTOMER_CARE/ATHENA_CC_PUBLIC/NRC_UPLOAD_MAX_SIZE", "10485760");
        String fileSrc = "";// 文件来源
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();

            String thresholdSize = ConfigurationMgr.instance().getString("upload.uploadThresholdSize");
            String repository = ConfigurationMgr.instance().getString("upload.uploadRepository");// TODO 没用到
            while (!repository.endsWith("\\\\") && !repository.endsWith("/")) {// 可能需要添加两次,所以用while
                repository += File.separator;
            }
            File dirRepository = new File(repository);
            if (!dirRepository.exists()) {
                dirRepository.mkdirs();
            }

            String fileDirectory = ConfigurationMgr.instance().getString("upload.uploadFileDirectory");
            while (!fileDirectory.endsWith("\\\\") && !fileDirectory.endsWith("/")) {// 可能需要添加两次,所以用while
                fileDirectory += File.separator;
            }
            File dirDirectory = new File(fileDirectory);
            if (!dirDirectory.exists()) {
                dirDirectory.mkdirs();
            }

            // Set factory constraints
            // 设置最多只允许在内存中存储的数据,单位:字节
            factory.setSizeThreshold(Integer.parseInt(thresholdSize));
            factory.setRepository(dirRepository);

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);
            // Set overall request size constraint
            upload.setSizeMax(Long.parseLong(fileSizeMax));

            // Parse the request
            List<FileItem> fileItems = upload.parseRequest(request);

            // 依次处理每个上传的文件
            Iterator iter = fileItems.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                // 忽略其他不是文件域的所有表单信息
                if (!item.isFormField()) {

                    String name = item.getName();
                    fileSrc = name;
                    int lastIndex = name.lastIndexOf("\\");
                    // get file name from full name
                    String fileName = name.substring(lastIndex + 1);

                    // 校验文件名
                    String errorCode = checkFileName(request, fileName);
                    if (StringUtil.isNotEmpty(errorCode)) {
                        errCodeGlobal = errorCode;
                        break;
                    }

                    fileName = getFileName(request, fileName);
                    /*
                     * 412554 if(fileName.indexOf(" ")!=-1||fileName.indexOf("+")!=-1||fileName.indexOf("-")!=-1){
                     * errCodeGlobal = "COMMON.UPLOAD_FILE_NAME_ERROR";
                     * logger.error("File name can not contains spaces or '+-' symbol!"); break; }
                     */
                    int intFileId = 0;
                    String strPathFileNameNew = fileDirectory + fileName;
                    File newFile = new File(strPathFileNameNew);
                    String fileSize = String.valueOf(item.getSize());

                    if (fileSize.equals("0")) {
                        errCodeGlobal = "COMMON.UPLOAD_FILE_CAN_NOT_BE_EMPTY";
                        logger.error("File can not be empty!");
                        break;
                    }

                    String strFileNameNew = "";// 新文件名
                    int index = -1;
                    // 如果当前文件存在,文件名后缀计数累计
                    while (newFile.exists()) {
                        intFileId += 1;
                        index = fileName.lastIndexOf(".");
                        if (index != -1) {
                            strFileNameNew = fileName.substring(0, index) + "_" + intFileId + "."
                                + fileName.substring(index + 1);
                        }
                        else {
                            strFileNameNew = fileName + "_" + intFileId;
                        }
                        strPathFileNameNew = fileDirectory + strFileNameNew;
                        newFile = new File(strPathFileNameNew);
                    }
                    if (StringUtil.isEmpty(strFileNameNew)) {
                        strFileNameNew = fileName;
                    }

                    // save file to directory of upload
                    item.write(newFile);
                    request.setAttribute("fileName", strFileNameNew);
                    request.setAttribute("fileSrc", fileSrc);
                    request.setAttribute("fileSize", fileSize);
                    request.setAttribute("filePath", strPathFileNameNew);

                    // 文件上传之后处理
                    afterUpload(request, strFileNameNew, fileDirectory, strPathFileNameNew);

                    logger.info("UploadFile success:[{}]", strPathFileNameNew);

                    JSONObject jsono = new JSONObject();
                    // String encodefileName = URLEncoder.encode(strFileNameNew, "UTF-8");
                    jsono.put("name", strFileNameNew);
                    jsono.put("fileName", strFileNameNew);
                    jsono.put("filePath", fileDirectory);
                    jsono.put("size", item.getSize());
                    json.add(jsono);
                }
            }
        }
        catch (SizeLimitExceededException ex) {
            logger.error(Common.getCommonRes("S-WEB-00001"), fileSizeMax, "kb", ex);
            errCodeGlobal = "COMMON.UPLOAD_FILE_EXCEED_MAX";
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            errCodeGlobal = "COMMON.UPLOAD_FAIL";
        }
        finally {
            // 异常信息加入输出
            if (errCodeGlobal != null) {
                JSONObject jsono = new JSONObject();
                jsono.put("error", errCodeGlobal);
                json.add(jsono);
            }
            writer.write(json.toString());
            writer.close();
        }
        // Common.gotoAnotherLink(this, request, response, errCodeGlobal, nextLink);
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param request <br>
     * @param fileName <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    private String checkFileName(HttpServletRequest request, String fileName) throws BaseAppException {
        String action = request.getParameter("action");
        String errCodeGlobal = null;
        if (StringUtil.isNotEmpty(action)) {
            if ("submitNRC".equals(action)) {
                if (!fileName.endsWith(".png") && !fileName.endsWith(".jpeg") && !fileName.endsWith(".jpg")
                    && !fileName.endsWith(".PNG") && !fileName.endsWith(".JPEG") && !fileName.endsWith(".JPG")) {
                    errCodeGlobal = "COMMON.IMG_FORMAT_ERROR";
                }
            }
        }
        return errCodeGlobal;
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param request <br>
     * @param fileName <br>
     * @return <br>
     * @throws BaseAppException <br>
     */
    private String getFileName(HttpServletRequest request, String fileName) throws BaseAppException {
        String action = request.getParameter("action");

        if (StringUtil.isNotEmpty(action)) {
            if ("submitNRC".equals(action)) {
                SessionDto session = (SessionDto) request.getSession().getAttribute(Common.USER_INFO_BEAN);
                fileName = request.getParameter("prefix") + "_" + fileName;
                fileName = session.getCustId() + "_" + fileName;
            }
        }
        return fileName;
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param request <br>
     * @param strPathFileNameNew <br>
     * @throws BaseAppException <br>
     */
    private void afterUpload(HttpServletRequest request, String fileName, String fileDirectory, String pathFileName)
        throws BaseAppException {
        String action = request.getParameter("action");

        if (StringUtil.isNotEmpty(action)) {
            if ("submitNRC".equals(action)) {
                // 需要将文件压缩，然后删除源文件
                reduceImg(fileName, fileDirectory, pathFileName);
            }
        }
    }

    /**
     * Description: <br>
     * 
     * @author zhang.liang<br>
     * @taskId <br>
     * @param fileName <br>
     * @param fileDirectory <br>
     * @param pathFileName <br>
     * @throws BaseAppException <br>
     */
    private void reduceImg(String fileName, String fileDirectory, String pathFileName) throws BaseAppException {
        // 压缩文件到200k,并删除源文件
        File file = new File(pathFileName);
        if (!(file.exists() && file.canRead())) {
            return;
        }
        
        long size = file.length();
        size = size / 1024;
        long maxSize = 200;
        float qulianty = 1f;
        if (size > maxSize) {
            // 计算压缩比
            qulianty = (float) maxSize / (float) size;
            DecimalFormat decimalFormat = new DecimalFormat(".00");
            String a = decimalFormat.format(qulianty);
            qulianty = Float.valueOf(a);
        }
        try {
            //added by chenshun 2016-3-22 注释掉之前长宽的方式，改用大小
//            Thumbnails.of(filePathName).size(width, height).toFile(thumbnailFilePathName);
            if(size < 200){
                Thumbnails.of(pathFileName).scale(1f).toFile(pathFileName);
            }else{
                Thumbnails.of(pathFileName).scale(1f).outputQuality(qulianty).toFile(pathFileName);
            }
//            file.delete();
        } 
        catch (Exception e1) {
            logger.error("Fail to reduceImg", e1);
        }
    }

    /**
     * 以JPEG编码保存图片
     * 
     * @param imageTosave 要处理的图像图片
     * @param quality 压缩比
     * @param fos 文件输出流
     * @throws IOException
     */
    public static void saveAsJPEG(BufferedImage imageTosave, float quality, FileOutputStream fos) throws IOException {

        // Image writer
        JPEGImageWriter imageWriter = (JPEGImageWriter) ImageIO.getImageWritersBySuffix("jpg").next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
        imageWriter.setOutput(ios);
        // and metadata
        IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(imageTosave), null);

        JPEGImageWriteParam jpegParams = null;
        if (quality >= 0 && quality <= 1f) {
            jpegParams = (JPEGImageWriteParam) imageWriter.getDefaultWriteParam();
            jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
            jpegParams.setCompressionQuality(quality);
        }

        imageWriter.write(imageMetaData, new IIOImage(imageTosave, null, null), jpegParams);
        ios.close();
        imageWriter.dispose();
    }  

    /**
     * Initialization of the servlet. <br>
     * 
     * @throws ServletException if an error occure
     */
    public void init() throws ServletException {
        //
    }
}

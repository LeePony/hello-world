module.exports = {
    debug:true,         //生成less sourcemap
    // entry: {
    //     main: 'main.js', //工程下requirejs的入口文件，也可以是entry:'main.js'
    // },
    // output: {
    //     filename: '[name].bundle.js',   // 输出合并解析后的文件名，[name]将由entry的key值替代
    //     path: ''                        // 输出的路径
    // },
    devServer:{
        port: 8181,     //server启动端口号设置，默认启动当前目录下index.html文件
        contentBase: '', //设置服务启动目录，默认为当前目录
        watch:{         //监听哪些文件，使得项目浏览器web能够自动刷新
            // less:[
            //     './**/*.less'
            // ],
            // other:[
            //     './**/*.html',
            //     './**/*.js',
            //     './**/*.hbs'
            // ]
        },
        proxy: [
            // {
            //     match: ['/**', '!**/*.js', '!/styles/**'], //需要被代理的url,这里意味着代理所有接口，但不包括所有js文件和styles文件夹下的文件 
            //     target: 'http://xxx:8080/', //代理的服务器地址，一般为测试服务器地址
            //     logLevel: 'debug' // 打印被代理url的信息
            // }
        ]
    },
    build:[
        // {
        //     match:['js/base.js','js/test.js'],       //编译文件路径
        //     transfer:'concat | uglify',             //编译方式：concat(合并)、uglify(压缩js)、less(编译less)、cssmin(压缩css)、image(压缩图片)
        //     filename:'minify.js',                    //编译后文件名minify.js
        //     dest:'dist'                           //编译输出路径
        // }
    ]
};
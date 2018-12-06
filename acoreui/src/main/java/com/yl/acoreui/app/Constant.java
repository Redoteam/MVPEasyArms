package com.yl.acoreui.app;

public interface Constant {
    /**
     * ------------------------intent constant------------------------------
     **/
    String KEY_STRING_1 = "KEY_STRING_1";
    String KEY_STRING_2 = "KEY_STRING_2";
    String KEY_INT_1 = "KEY_INT_1";
    String KEY_INT_2 = "KEY_INT_2";
    String KEY_INT_3 = "KEY_INT_3";
    String KEY_INT_4 = "KEY_INT_4";
    String KEY_BEAN = "KEY_BEAN";
    String KEY_BEAN_2 = "KEY_BEAN_2";
    String KEY_LIST = "KEY_LIST";

    //设置WebView图片的宽度
    String HTML_START_WITH_CLICK = "<html><head><style type=\"text/css\"> img{width:50%;height:auto} body{font-size:20px;line-height:1.7;color:#585858}</style> <script type=\"text/javascript\"> " + "function imageOnclick(){ " + "var objs = document.getElementsByTagName(\"img\");" + " var array=new Array(); " + " for(var j=0;j<objs.length;j++){ " + "array[j]=objs[j].src;" + " }  " + "for(var i=0;i<objs.length;i++){" + "objs[i].i=i;" + "objs[i].onclick=function(){  window.imagelistener.openImage(this.src,array,this.i);" + "}  " + "" + "}" + "}" + "window.onload =  function(){ imageOnclick();  }" + "</script> </head><body>";
    String HTML_END = "</body></html>";
    /**
     * ------------------------web end------------------------------------
     **/

    String CONTENT_TYPE_GRAPHIC = "图文";
    String CONTENT_TYPE_TEXT = "文字";
    String CONTENT_TYPE_ALBUN = "相册";
    String CONTENT_TYPE_FULL_VIDEO = "全屏视频";
    String CONTENT_TYPE_MONITOR = "监控";
}

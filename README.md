# MVPEasyArms
[![](https://jitpack.io/v/Redoteam/MVPEasyArms.svg)](https://jitpack.io/#Redoteam/MVPEasyArms)

# step 1

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  
 # step 2
  
  	dependencies {
	
	        implementation 'com.github.Redoteam.MVPEasyArms:acore:v1.0.1'//基础框架
          	implementation 'com.github.Redoteam.MVPEasyArms:core-imageloader-glide:v1.0.1'//图片加载配合acore使用
          
	        implementation 'com.github.Redoteam.MVPEasyArms:acoreui:v1.0.1'//封装的一些Base
	        implementation 'com.github.Redoteam.MVPEasyArms:video:v1.0.1'//基于GSY视频框架
	}


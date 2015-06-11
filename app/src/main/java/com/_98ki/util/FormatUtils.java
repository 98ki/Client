/**    
 * file name：FormatUtils.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-11-13 
 * @version:    
 * Copyright zpy@98ki.com Corporation 2014         
 *    
 */
package com._98ki.util;

import android.content.Context;

/**    
 *     
 * project：LaoYouBang    
 * class：FormatUtils    
 * desc：    Data format util
 * author：zpy zpy@98ki.com    
 * create date：2014-11-13 下午9:26:28    
 * modify author: zpy    
 * update date：2014-11-13 下午9:26:28    
 * update remark：    
 * @version     
 *     
 */
public class FormatUtils {

	 /** 
     * dip 2 px
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * px 2 dip
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
}

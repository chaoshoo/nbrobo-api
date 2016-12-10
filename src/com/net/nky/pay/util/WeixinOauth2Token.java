 package com.net.nky.pay.util;
 
 public class WeixinOauth2Token
 {
   private String accessToken;
   private int expiresIn;
   private String refreshToken;
   private String openId;
   private String scope;
 
   public String getAccessToken()
   {
     return this.accessToken;
   }
 
   public void setAccessToken(String accessToken) {
     this.accessToken = accessToken;
   }
 
   public int getExpiresIn() {
     return this.expiresIn;
   }
 
   public void setExpiresIn(int expiresIn) {
     this.expiresIn = expiresIn;
   }
 
   public String getRefreshToken() {
     return this.refreshToken;
   }
 
   public void setRefreshToken(String refreshToken) {
     this.refreshToken = refreshToken;
   }
 
   public String getOpenId() {
     return this.openId;
   }
 
   public void setOpenId(String openId) {
     this.openId = openId;
   }
 
   public String getScope() {
     return this.scope;
   }
 
   public void setScope(String scope) {
     this.scope = scope;
   }
 }

/* Location:           
 * Qualified Name:     com.yq.weixin.pay.util.WeixinOauth2Token
 * JD-Core Version:    0.6.2
 */
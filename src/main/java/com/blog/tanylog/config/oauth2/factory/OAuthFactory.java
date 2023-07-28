package com.blog.tanylog.config.oauth2.factory;

import com.blog.tanylog.config.oauth2.providerType.CheckOAuthProvider;
import com.blog.tanylog.config.oauth2.providerType.ProviderByKakao;
import com.blog.tanylog.config.oauth2.providerType.ProviderByNaver;

public class OAuthFactory implements ProviderFactory {

  @Override
  public CheckOAuthProvider getProvider(String provider) {
    if (provider.equals("naver")) {
      return new ProviderByNaver();

    } else if (provider.equals("kakao")) {
      return new ProviderByKakao();
    }

    return null;
  }
}

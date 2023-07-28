package com.blog.tanylog.config.oauth2.factory;

import com.blog.tanylog.config.oauth2.providerType.CheckOAuthProvider;

public interface ProviderFactory {

  CheckOAuthProvider getProvider(String provider);
}

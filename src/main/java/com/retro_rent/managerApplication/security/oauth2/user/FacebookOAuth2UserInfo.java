package com.retro_rent.managerApplication.security.oauth2.user;

import org.springframework.util.StringUtils;

import java.util.Map;

public class FacebookOAuth2UserInfo extends OAuth2UserInfo {
    public FacebookOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getLastName() {
        return (String) attributes.get("last_name");
    }

    @Override
    public String getName() {
        return (String) attributes.get("first_name");
    }

    @Override
    public String getEmail() {
        if (StringUtils.isEmpty(attributes.get("email"))) {
                return attributes.get("name") + "@facebook.com"; }
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        if(attributes.containsKey("picture")) {
            Map<String, Object> pictureObj = (Map<String, Object>) attributes.get("picture");
            if(pictureObj.containsKey("data")) {
                Map<String, Object>  dataObj = (Map<String, Object>) pictureObj.get("data");
                if(dataObj.containsKey("url")) {
                    return (String) dataObj.get("url");
                }
            }
        }
        return null;
    }
}

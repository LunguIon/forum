package md.forum.forum.security.model;

import java.util.Map;

public class GoogleUserInfo {
    private Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    public String getEmail() {
        return (String) attributes.get("email");
    }
    public String getId() {
        return (String) attributes.get("id");
    }
    public String getName(){
        return (String) attributes.get("name");
    }
    public String getProfileImageUrl() {
        return (String) attributes.get("profile_image_url");
    }
}

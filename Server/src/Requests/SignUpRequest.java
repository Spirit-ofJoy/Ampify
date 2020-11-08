package Requests;

import constants.Constant;

public class SignUpRequest extends Request{

    private String username;
    private String password;
    private String preferenceLanguage;
    private String preferenceGenre;
    private String preferenceArtists;

    public SignUpRequest(String uname, String paswd, String prefLang, String prefGenre, String prefArtist) {
        this.reqType = (Constant.SIGNUP_START);
        this.username = uname;
        this.password = paswd;
        this.preferenceLanguage = prefLang;
        this.preferenceGenre = prefGenre;
        this.preferenceArtists = prefArtist;
    }

    @Override
    public Constant getReqType() {
        return this.reqType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPreferenceLanguage() {
        return preferenceLanguage;
    }

    public String getPreferenceGenre() {
        return preferenceGenre;
    }

    public String getPreferenceArtists() {
        return preferenceArtists;
    }
}

package constants;


public enum Constant {

    //Types of Requests and Response
    SIGNUP_START,
    LOGIN_CHECK,
    TOP_HITS_LIST,
    NEW_RELEASES_LIST,
    PERSONAL_RECOMMENDS,
    SONG_BROWSE,
    SONG_SEARCH,
    HISTORY_INFO,
    PERSONAL_PLAYLISTS_SET,
    SHARE_PLAYLISTS_SET,
    UPDATE_PLAYLIST,
    CREATE_PLAYLIST,
    IMPORT_PLAYLIST,
    USERS_LIST,
    SONG_PLAYED,
    SONG_LIKED,
    CREATE_GROUP,
    PERSONAL_GROUPS_SET,
    LOAD_GROUP,
    LOAD_GROUP_CHATS,
    CHAT_SEND,


    //Types of execution state
    FAILURE,
    SUCCESS,
    USER_NOT_FOUND,

    //Types of Search Song Requests/Responses
    Artist,
    Album,
    All_Songs;

}

/*CREATE TABLE IF NOT EXISTS USERS_AUTHORITY (
    AUTHORITY_ID NUMBER NOT NULL UNIQUE,
    USER_ROLE VARCHAR2(128 CHAR) NOT NULL,
    CONSTRAINT PK_AUTH PRIMARY KEY (AUTHORITY_ID)
);

CREATE TABLE IF NOT EXISTS USERS (
    USER_ID NUMBER NOT NULL UNIQUE,
    USERNAME VARCHAR2(128 CHAR) NOT NULL,
    PASSWORD VARCHAR2(128 CHAR) NOT NULL,
    ENABLED BOOLEAN NOT NULL,
    ACCOUNT_NON_EXPIRED BOOLEAN NOT NULL,
    CREDENTIALS_NON_EXPIRED BOOLEAN NOT NULL,
    ACCOUNT_NON_LOCKED BOOLEAN NOT NULL,
    CREDENTIAL_UPDATED VARCHAR2(24 CHAR) NOT NULL,
    CONSTRAINT PK_AUTH PRIMARY KEY (USER_ID)
);*/

CREATE TABLE IF NOT EXISTS INSTANCES (
    INSTANCE_ID VARCHAR2(128 CHAR) NOT NULL UNIQUE,
    INSTANCE_NAME VARCHAR2(128 CHAR) NOT NULL,
    CONTAINER_IMAGE VARCHAR2(128 CHAR) NOT NULL,
    EVENT_ID VARCHAR2(128 CHAR) NOT NULL,
    EVENT_RULES_ID VARCHAR2(128 CHAR) NOT NULL,
    ENTRIES_ID VARCHAR2(128 CHAR) NOT NULL,
    ASSIST_RULES_ID VARCHAR2(128 CHAR) NOT NULL,
    BOP_ID VARCHAR2(128 CHAR) NOT NULL,
    CONFIG_ID VARCHAR2(128 CHAR) NOT NULL,
    SETTINGS_ID VARCHAR2(128 CHAR) NOT NULL,
    CONSTRAINT PK_INSTANCE PRIMARY KEY (INSTANCE_ID)
);

CREATE TABLE IF NOT EXISTS EVENT (
    EVENT_ID VARCHAR2(128 CHAR) NOT NULL,
    EVENT_NAME VARCHAR2(256 CHAR) NOT NULL,
    TRACK VARCHAR2(128 CHAR) NOT NULL,
    PRE_RACE_WAITING_TIME_SEC NUMBER(10,0) NOT NULL,
    SESSION_OVER_TIME_SEC NUMBER(10,0) NOT NULL,
    AMBIENT_TEMP NUMBER(2,0) NOT NULL,
    TRACK_TEMP NUMBER(2,0) NOT NULL,
    CLOUD_LEVEL FLOAT NOT NULL,
    RAIN FLOAT NOT NULL,
    WEATHER_RANDOMNESS NUMBER(1,0) NOT NULL,
    POST_QUALY_TIME_SEC NUMBER(5,0) NOT NULL,
    POST_RACE_TIME_SEC NUMBER(5,0) NOT NULL,
    META_DATA VARCHAR2(128 CHAR) NOT NULL,
    SESSION_ID VARCHAR2(128 CHAR) NOT NULL,
    CONSTRAINT PK_EVENT PRIMARY KEY (EVENT_ID),
    CONSTRAINT FK1_EVENT FOREIGN KEY (EVENT_ID)
    REFERENCES INSTANCES (EVENT_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS EVENT_RULES (
    EVENT_RULES_ID VARCHAR2(128 CHAR) NOT NULL,
    QUALIFY_STANDING_TYPE NUMBER(1,0) NOT NULL,
    SUPERPOLE_MAX_CAR NUMBER(1,0) NOT NULL,
    PIT_WINDOW_LENGTH_SEC NUMBER(3,0) NOT NULL,
    DRIVER_STINT_TIME_SEC NUMBER(1,0) NOT NULL,
    MANDATORY_PITSTOP_COUNT NUMBER(1,0) NOT NULL,
    MAX_TOTAL_DRIVING_TIME NUMBER(4,0) NOT NULL,
    MAX_DRIVERS_COUNT NUMBER(2,0) NOT NULL,
    IS_REFUELLING_ALLOWED_IN_RACE NUMBER(1,0) NOT NULL,
    IS_REFUELLING_TIME_FIXED NUMBER(1,0) NOT NULL,
    IS_MANDATORY_PITSTOP_REFUELLING_REQUIRED NUMBER(1,0) NOT NULL,
    IS_MANDATORY_PITSTOP_TYRE_CHANGE_REQUIRED NUMBER(1,0) NOT NULL,
    IS_MANDATORY_PITSTOP_SWAP_DRIVER_REQUIRED NUMBER(1,0) NOT NULL,
    TYRE_SET_COUNT NUMBER(2,0) NOT NULL,
    CONSTRAINT PK_EVENT_RULES PRIMARY KEY (EVENT_RULES_ID),
    CONSTRAINT FK1_EVENT_RULES FOREIGN KEY (EVENT_RULES_ID)
    REFERENCES INSTANCES (EVENT_RULES_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS SESSIONS (
    SESSION_ID VARCHAR2(128 CHAR) NOT NULL,
    EVENT_ID VARCHAR2(128 CHAR) NOT NULL,
    HOUR_OF_DAY NUMBER(2,0) NOT NULL,
    DAY_OF_WEEKEND NUMBER(2,0) NOT NULL,
    TIME_MULTIPLIER NUMBER(2,0) NOT NULL,
    SESSION_TYPE VARCHAR2(1 CHAR) NOT NULL,
    SESSION_DURATION_MIN NUMBER(5,0) NOT NULL,
    CONSTRAINT PK_SESSIONS PRIMARY KEY (SESSION_ID),
    CONSTRAINT FK1_SESSIONS FOREIGN KEY (EVENT_ID)
    REFERENCES EVENT (EVENT_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS CAR_ENTRIES (
    CAR_ENTRIES_ID VARCHAR2(128 CHAR) NOT NULL,
    CAR_ENTRY_ID VARCHAR2(128 CHAR) NOT NULL,
    FORCE_ENTRY_LIST NUMBER(1,0) NOT NULL,
    CONFIG_VERSION NUMBER(1,0) NOT NULL,
    CONSTRAINT PK_CAR_ENTRIES PRIMARY KEY (CAR_ENTRIES_ID),
    CONSTRAINT FK1_CAR_ENTRIES FOREIGN KEY (CAR_ENTRIES_ID)
    REFERENCES INSTANCES (ENTRIES_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS CAR_ENTRY (
    CAR_ENTRY_ID VARCHAR2(128 CHAR) NOT NULL,
    CAR_ENTRIES_ID VARCHAR2(128 CHAR) NOT NULL,
    DRIVER_ID VARCHAR2(128 CHAR) NOT NULL,
    RACE_NUMBER NUMBER(5,0) NOT NULL,
    FORCE_CAR_MODEL NUMBER(1,0) NOT NULL,
    OVERRIDE_DRIVER_INFO NUMBER(1,0) NOT NULL,
    CUSTOM_CAR VARCHAR2(50 CHAR),
    OVERRIDE_CAR_MODEL_FOR_CUSTOM_CAR NUMBER(1,0),
    IS_SERVER_ADMIN NUMBER(1,0) NOT NULL,
    DEFAULT_GRID_POSITION NUMBER(1,0),
    BALLAST_KG NUMBER(1,0),
    RESTRICTOR NUMBER(1,0),
    CONSTRAINT PK_CAR_ENTRY PRIMARY KEY (CAR_ENTRY_ID),
    CONSTRAINT FK1_CAR_ENTRY FOREIGN KEY (CAR_ENTRIES_ID)
    REFERENCES CAR_ENTRIES (CAR_ENTRIES_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS DRIVER (
    DRIVER_ID VARCHAR2(128 CHAR) NOT NULL,
    CAR_ENTRY_ID VARCHAR2(128 CHAR) NOT NULL,
    FIRST_NAME VARCHAR2(50 CHAR),
    LAST_NAME VARCHAR2(50 CHAR),
    SHORT_NAME VARCHAR2(50 CHAR),
    DRIVER_CATEGORY NUMBER(1,0),
    PLAYER_ID VARCHAR2(50 CHAR) NOT NULL,
    CONSTRAINT PK_DRIVER PRIMARY KEY (DRIVER_ID),
    CONSTRAINT FK1_DRIVER FOREIGN KEY (CAR_ENTRY_ID)
    REFERENCES CAR_ENTRY (CAR_ENTRY_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ASSIST_RULES (
    ASSIST_RULES_ID VARCHAR2(128 CHAR) NOT NULL,
    STABILITY_CONTROL_LEVEL_MAX NUMBER(3,0) NOT NULL,
    DISABLE_AUTO_STEER NUMBER(1,0) NOT NULL,
    DISABLE_AUTO_LIGHTS NUMBER(1,0) NOT NULL,
    DISABLE_AUTO_WIPER NUMBER(1,0) NOT NULL,
    DISABLE_AUTO_ENGINE_START NUMBER(1,0) NOT NULL,
    DISABLE_AUTO_PIT_LIMITER NUMBER(1,0) NOT NULL,
    DISABLE_AUTO_GEAR NUMBER(1,0) NOT NULL,
    DISABLE_AUTO_CLUTCH NUMBER(1,0) NOT NULL,
    DISABLE_IDEAL_LINE NUMBER(1,0) NOT NULL,
    CONSTRAINT PK_ASSISTS PRIMARY KEY (ASSIST_RULES_ID),
    CONSTRAINT FK1_ASSISTS FOREIGN KEY (ASSIST_RULES_ID)
    REFERENCES INSTANCES (ASSIST_RULES_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS BOP (
    BOP_ID VARCHAR2(128 CHAR) NOT NULL,
    BOP_ENTRY_ID VARCHAR2(128 CHAR) NOT NULL,
    DISABLE_AUTO_STEER NUMBER(1,0) NOT NULL,
    DISABLE_AUTO_LIGHTS NUMBER(1,0) NOT NULL,
    DISABLE_AUTO_WIPER NUMBER(1,0) NOT NULL,
    CONSTRAINT PK_BOP PRIMARY KEY (BOP_ID),
    CONSTRAINT FK1_BOP FOREIGN KEY (BOP_ID)
    REFERENCES INSTANCES (BOP_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS BOP_ENTRY (
  BOP_ENTRY_ID VARCHAR2(128 CHAR) NOT NULL,
  BOP_ID VARCHAR2(128 CHAR) NOT NULL,
  TRACK VARCHAR2(50 CHAR) NOT NULL,
  CAR_MODEL NUMBER(3,0) NOT NULL,
  BALLEST NUMBER(3,0),
  RESTRICTOR NUMBER(2,0),
  CONSTRAINT PK_BOP_ENTRY PRIMARY KEY (BOP_ENTRY_ID),
  CONSTRAINT FK1_BOP_ENTRY FOREIGN KEY (BOP_ID)
  REFERENCES BOP (BOP_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS CONFIG (
  CONFIG_ID VARCHAR2(128 CHAR) NOT NULL,
  UDP_PORT NUMBER(9,0) NOT NULL,
  TCP_PORT NUMBER(9,0) NOT NULL,
  MAX_CONNECTIONS NUMBER(3,0) NOT NULL,
  LAN_DISCOVERY NUMBER(1,0) NOT NULL,
  REGISTER_TO_LOBBY NUMBER(1,0) NOT NULL,
  PUBLIC_IP VARCHAR2(16 CHAR) NOT NULL,
  CONFIG_VERSION NUMBER(1,0) NOT NULL,
  CONSTRAINT PK_CONFIG PRIMARY KEY (CONFIG_ID),
  CONSTRAINT FK1_CONFIG FOREIGN KEY (CONFIG_ID)
  REFERENCES INSTANCES (CONFIG_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS SETTINGS (
  SETTINGS_ID VARCHAR2(128 CHAR) NOT NULL,
  SERVER_INSTANCE_NAME VARCHAR2(128 CHAR) NOT NULL,
  ADMIN_PASSWORD VARCHAR2(128 CHAR) NOT NULL,
  CAR_GROUP VARCHAR2(20 CHAR) NOT NULL,
  TRACK_MEDALS_REQUIREMENT NUMBER(3,0) NOT NULL,
  SAFETY_RATING_REQUIREMENT NUMBER(3,0) NOT NULL,
  RACE_CRAFT_RATING_REQUIREMENT NUMBER(3,0) NOT NULL,
  SERVER_PASSWORD VARCHAR2(128 CHAR) NOT NULL,
  SPECTATOR_PASSWORD VARCHAR2(128 CHAR) NOT NULL,
  MAX_CAR_SLOTS NUMBER(3,0) NOT NULL,
  DUMP_LEADER_BOARDS NUMBER(1,0) NOT NULL,
  IS_RACE_LOCKED NUMBER(1,0) NOT NULL,
  IS_PREP_PHASE_LOCKED NUMBER(1,0) NOT NULL,
  RANDOMIZE_TRACK_WHEN_EMPTY NUMBER(1,0) NOT NULL,
  CENTRAL_ENTRY_LIST_PATH VARCHAR2(128 CHAR) NOT NULL,
  ALLOW_AUTO_DQ NUMBER(1,0) NOT NULL,
  SHORT_FORMATION_LAP NUMBER(1,0) NOT NULL,
  DUMP_ENTRY_LIST NUMBER(1,0) NOT NULL,
  FORMATION_LAP_TYPE NUMBER(1,0) NOT NULL,
  DO_DRIVER_SWAP_BROADCAST NUMBER(1,0) NOT NULL,
  CONFIG_VERSION NUMBER(1,0) NOT NULL,
  CONSTRAINT PK_SETTINGS PRIMARY KEY (SETTINGS_ID),
  CONSTRAINT FK1_SETTINGS FOREIGN KEY (SETTINGS_ID)
  REFERENCES INSTANCES (SETTINGS_ID) ON DELETE CASCADE
);
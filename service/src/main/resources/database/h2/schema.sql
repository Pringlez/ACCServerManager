CREATE TABLE ASSIST_RULES (
    ASSIST_RULES_ID CHARACTER VARYING(255) NOT NULL,
    DISABLE_AUTO_CLUTCH INTEGER,
    DISABLE_AUTO_ENGINE_START INTEGER,
    DISABLE_AUTO_GEAR INTEGER,
    DISABLE_AUTO_LIGHTS INTEGER,
    DISABLE_AUTO_PIT_LIMITER INTEGER,
    DISABLE_AUTO_STEER INTEGER,
    DISABLE_AUTO_WIPER INTEGER,
    DISABLE_IDEAL_LINE INTEGER,
    STABILITY_CONTROL_LEVEL_MAX INTEGER
);
ALTER TABLE ASSIST_RULES ADD CONSTRAINT CONSTRAINT_1 PRIMARY KEY (ASSIST_RULES_ID);

CREATE TABLE BOP (
    BOP_ID CHARACTER VARYING(255) NOT NULL,
    BOP_ENTRY_ID CHARACTER VARYING(255),
    DISABLE_AUTO_LIGHTS INTEGER,
    DISABLE_AUTO_STEER INTEGER,
    DISABLE_AUTO_WIPER INTEGER
);
ALTER TABLE BOP ADD CONSTRAINT CONSTRAINT_2 PRIMARY KEY (BOP_ID);

CREATE TABLE BOP_ENTRY (
    BOP_ENTRY_ID CHARACTER VARYING(255) NOT NULL,
    BALLEST INTEGER,
    BOP_ID CHARACTER VARYING(255),
    CAR_MODEL INTEGER,
    RESTRICTOR INTEGER,
    TRACK CHARACTER VARYING(255)
);
ALTER TABLE BOP_ENTRY ADD CONSTRAINT CONSTRAINT_3 PRIMARY KEY (BOP_ENTRY_ID);

CREATE TABLE CAR_ENTRIES (
    CAR_ENTRIES_ID CHARACTER VARYING(255) NOT NULL,
    CAR_ENTRY_ID CHARACTER VARYING(255),
    CONFIG_VERSION INTEGER,
    FORCE_ENTRY_LIST INTEGER
);
ALTER TABLE CAR_ENTRIES ADD CONSTRAINT CONSTRAINT_4 PRIMARY KEY (CAR_ENTRIES_ID);

CREATE TABLE CAR_ENTRY (
    CAR_ENTRY_ID CHARACTER VARYING(255) NOT NULL,
    BALLAST_KG INTEGER,
    CAR_ENTRIES_ID CHARACTER VARYING(255),
    CUSTOM_CAR CHARACTER VARYING(255),
    DEFAULT_GRID_POSITION INTEGER,
    DRIVER_ID CHARACTER VARYING(255),
    FORCE_CAR_MODEL INTEGER,
    IS_SERVER_ADMIN INTEGER,
    OVERRIDE_CAR_MODEL_FOR_CUSTOM_CAR INTEGER,
    OVERRIDE_DRIVER_INFO INTEGER,
    RACE_NUMBER INTEGER,
    RESTRICTOR INTEGER
);
ALTER TABLE CAR_ENTRY ADD CONSTRAINT CONSTRAINT_5 PRIMARY KEY (CAR_ENTRY_ID);

CREATE TABLE CONFIG (
    CONFIG_ID CHARACTER VARYING(255) NOT NULL,
    CONFIG_VERSION INTEGER,
    LAN_DISCOVERY INTEGER,
    MAX_CONNECTIONS INTEGER,
    PUBLIC_IP CHARACTER VARYING(255),
    REGISTER_TO_LOBBY INTEGER,
    TCP_PORT INTEGER,
    UDP_PORT INTEGER
);
ALTER TABLE CONFIG ADD CONSTRAINT CONSTRAINT_6 PRIMARY KEY (CONFIG_ID);

CREATE TABLE DRIVER (
    DRIVER_ID CHARACTER VARYING(255) NOT NULL,
    CAR_ENTRY_ID CHARACTER VARYING(255),
    DRIVER_CATEGORY INTEGER,
    FIRST_NAME CHARACTER VARYING(255),
    LAST_NAME CHARACTER VARYING(255),
    PLAYER_ID CHARACTER VARYING(255),
    SHORT_NAME CHARACTER VARYING(255)
);
ALTER TABLE DRIVER ADD CONSTRAINT CONSTRAINT_7 PRIMARY KEY (DRIVER_ID);

CREATE TABLE EVENT (
    EVENT_ID CHARACTER VARYING(255) NOT NULL,
    AMBIENT_TEMP INTEGER,
    CLOUD_LEVEL DOUBLE PRECISION,
    EVENT_NAME CHARACTER VARYING(255),
    META_DATA CHARACTER VARYING(255),
    POST_QUALY_TIME_SEC INTEGER,
    POST_RACE_TIME_SEC INTEGER,
    PRE_RACE_WAITING_TIME_SEC INTEGER,
    RAIN DOUBLE PRECISION,
    SESSION_OVER_TIME_SEC INTEGER,
    TRACK CHARACTER VARYING(255),
    TRACK_TEMP INTEGER,
    WEATHER_RANDOMNESS INTEGER
);
ALTER TABLE EVENT ADD CONSTRAINT CONSTRAINT_8 PRIMARY KEY (EVENT_ID);

CREATE TABLE EVENT_RULES (
    EVENT_RULES_ID CHARACTER VARYING(255) NOT NULL,
    DRIVER_STINT_TIME_SEC INTEGER,
    IS_MANDATORY_PITSTOP_REFUELLING_REQUIRED BOOLEAN,
    IS_MANDATORY_PITSTOP_SWAP_DRIVER_REQUIRED BOOLEAN,
    IS_MANDATORY_PITSTOP_TYRE_CHANGE_REQUIRED BOOLEAN,
    IS_REFUELLING_ALLOWED_IN_RACE BOOLEAN,
    IS_REFUELLING_TIME_FIXED BOOLEAN,
    MANDATORY_PITSTOP_COUNT INTEGER,
    MAX_DRIVERS_COUNT INTEGER,
    MAX_TOTAL_DRIVING_TIME INTEGER,
    PIT_WINDOW_LENGTH_SEC INTEGER,
    QUALIFY_STANDING_TYPE INTEGER,
    SUPERPOLE_MAX_CAR INTEGER,
    TYRE_SET_COUNT INTEGER
);
ALTER TABLE EVENT_RULES ADD CONSTRAINT CONSTRAINT_9 PRIMARY KEY (EVENT_RULES_ID);

CREATE TABLE INSTANCES (
    INSTANCE_ID CHARACTER VARYING(255) NOT NULL,
    ASSIST_RULES_ID CHARACTER VARYING(255),
    BOP_ID CHARACTER VARYING(255),
    CONFIG_ID CHARACTER VARYING(255),
    CONTAINER_IMAGE CHARACTER VARYING(255),
    ENTRIES_ID CHARACTER VARYING(255),
    EVENT_ID CHARACTER VARYING(255),
    EVENT_RULES_ID CHARACTER VARYING(255),
    INSTANCE_NAME CHARACTER VARYING(255),
    SETTINGS_ID CHARACTER VARYING(255)
);
ALTER TABLE INSTANCES ADD CONSTRAINT CONSTRAINT_10 PRIMARY KEY (INSTANCE_ID);

CREATE TABLE ROLES (
    ROLE_ID CHARACTER VARYING(255) NOT NULL,
    ROLE_NAME CHARACTER VARYING(255)
);
ALTER TABLE ROLES ADD CONSTRAINT CONSTRAINT_11 PRIMARY KEY (ROLE_ID);

CREATE TABLE SESSIONS (
    SESSION_ID CHARACTER VARYING(255) NOT NULL,
    DAY_OF_WEEKEND INTEGER,
    EVENT_ID CHARACTER VARYING(255),
    HOUR_OF_DAY INTEGER,
    SESSION_DURATION_MIN INTEGER,
    SESSION_TYPE CHARACTER VARYING(255),
    TIME_MULTIPLIER INTEGER
);
ALTER TABLE SESSIONS ADD CONSTRAINT CONSTRAINT_12 PRIMARY KEY (SESSION_ID);

CREATE TABLE SETTINGS (
    SETTINGS_ID CHARACTER VARYING(255) NOT NULL,
    ADMIN_PASSWORD CHARACTER VARYING(255),
    ALLOW_AUTO_DQ INTEGER,
    CAR_GROUP CHARACTER VARYING(255),
    CENTRAL_ENTRY_LIST_PATH CHARACTER VARYING(255),
    CONFIG_VERSION INTEGER,
    DO_DRIVER_SWAP_BROADCAST INTEGER,
    DUMP_ENTRY_LIST INTEGER,
    DUMP_LEADER_BOARDS INTEGER,
    FORMATION_LAP_TYPE INTEGER,
    IS_PREP_PHASE_LOCKED INTEGER,
    IS_RACE_LOCKED INTEGER,
    MAX_CAR_SLOTS INTEGER,
    RACE_CRAFT_RATING_REQUIREMENT INTEGER,
    RANDOMIZE_TRACK_WHEN_EMPTY INTEGER,
    SAFETY_RATING_REQUIREMENT INTEGER,
    SERVER_INSTANCE_NAME CHARACTER VARYING(255),
    SERVER_PASSWORD CHARACTER VARYING(255),
    SHORT_FORMATION_LAP INTEGER,
    SPECTATOR_PASSWORD CHARACTER VARYING(255),
    TRACK_MEDALS_REQUIREMENT INTEGER
);
ALTER TABLE SETTINGS ADD CONSTRAINT CONSTRAINT_13 PRIMARY KEY (SETTINGS_ID);

CREATE TABLE USERS (
    USER_ID CHARACTER VARYING(255) NOT NULL,
    USERNAME CHARACTER VARYING(255),
    PASSWORD CHARACTER VARYING(255),
    IS_TEST_USER BOOLEAN,
    TOKEN_VALIDATION TIMESTAMP,
    USER_CREATION TIMESTAMP,
    ENABLED BOOLEAN,
    ACCOUNT_NON_EXPIRED BOOLEAN,
    CREDENTIALS_NON_EXPIRED BOOLEAN,
    ACCOUNT_NON_LOCKED BOOLEAN,
    CREDENTIAL_UPDATED TIMESTAMP
);
ALTER TABLE USERS ADD CONSTRAINT CONSTRAINT_14 PRIMARY KEY (USER_ID);

CREATE TABLE USERS_AUTHORITY (
    AUTHORITY_ID CHARACTER VARYING(255) NOT NULL,
    PERMISSION CHARACTER VARYING(255)
);
ALTER TABLE USERS_AUTHORITY ADD CONSTRAINT CONSTRAINT_15 PRIMARY KEY (AUTHORITY_ID);

CREATE TABLE USERS_VALIDATION (
    USER_ID CHARACTER VARYING(255) NOT NULL,
    PASSWORD_RESET_TOKEN CHARACTER VARYING(255),
    PASSWORD_RESET_ISSUED TIMESTAMP,
    CREATION TIMESTAMP,
    TOKEN CHARACTER VARYING(255),
    TOKEN_ISSUED TIMESTAMP
);

CREATE TABLE USERS_ROLES (
    USER_ID CHARACTER VARYING(255) NOT NULL,
    ROLE_ID CHARACTER VARYING(255) NOT NULL
);
ALTER TABLE USERS_ROLES ADD CONSTRAINT CONSTRAINT_16 FOREIGN KEY (ROLE_ID) REFERENCES ROLES (ROLE_ID) NOCHECK;
ALTER TABLE USERS_ROLES ADD CONSTRAINT CONSTRAINT_17 FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID) NOCHECK;
ALTER TABLE USERS_ROLES ADD CONSTRAINT CONSTRAINT_18 PRIMARY KEY (USER_ID, ROLE_ID);

CREATE TABLE USERS_ROLES_AUTHORITIES (
    ROLE_ID CHARACTER VARYING(255) NOT NULL,
    AUTHORITY_ID CHARACTER VARYING(255) NOT NULL
);
-- ALTER TABLE USERS_ROLES_AUTHORITIES ADD CONSTRAINT CONSTRAINT_19 FOREIGN KEY (AUTHORITY_ID) REFERENCES USERS_AUTHORITY (AUTHORITY_ID) NOCHECK;
-- ALTER TABLE USERS_ROLES_AUTHORITIES ADD CONSTRAINT CONSTRAINT_20 FOREIGN KEY (ROLE_ID) REFERENCES ROLES (ROLE_ID) NOCHECK;
ALTER TABLE USERS_ROLES_AUTHORITIES ADD CONSTRAINT CONSTRAINT_21 PRIMARY KEY (ROLE_ID, AUTHORITY_ID);

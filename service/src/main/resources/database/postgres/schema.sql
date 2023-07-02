create table assist_rules
(
    assist_rules_id             varchar(255) not null
        primary key,
    disable_auto_clutch         integer,
    disable_auto_engine_start   integer,
    disable_auto_gear           integer,
    disable_auto_lights         integer,
    disable_auto_pit_limiter    integer,
    disable_auto_steer          integer,
    disable_auto_wiper          integer,
    disable_ideal_line          integer,
    stability_control_level_max integer
);

alter table assist_rules
    owner to postgres;

create table bop
(
    bop_id              varchar(255) not null
        primary key,
    bop_entry_id        varchar(255),
    disable_auto_lights integer,
    disable_auto_steer  integer,
    disable_auto_wiper  integer
);

alter table bop
    owner to postgres;

create table bop_entry
(
    bop_entry_id varchar(255) not null
        primary key,
    ballest      integer,
    bop_id       varchar(255),
    car_model    integer,
    restrictor   integer,
    track        varchar(255)
);

alter table bop_entry
    owner to postgres;

create table car_entries
(
    car_entries_id   varchar(255) not null
        primary key,
    car_entry_id     varchar(255),
    config_version   integer,
    force_entry_list integer
);

alter table car_entries
    owner to postgres;

create table car_entry
(
    car_entry_id                      varchar(255) not null
        primary key,
    ballast_kg                        integer,
    car_entries_id                    varchar(255),
    custom_car                        varchar(255),
    default_grid_position             integer,
    driver_id                         varchar(255),
    force_car_model                   integer,
    is_server_admin                   integer,
    override_car_model_for_custom_car integer,
    override_driver_info              integer,
    race_number                       integer,
    restrictor                        integer
);

alter table car_entry
    owner to postgres;

create table config
(
    config_id         varchar(255) not null
        primary key,
    config_version    integer,
    lan_discovery     integer,
    max_connections   integer,
    public_ip         varchar(255),
    register_to_lobby integer,
    tcp_port          integer,
    udp_port          integer
);

alter table config
    owner to postgres;

create table driver
(
    driver_id       varchar(255) not null
        primary key,
    car_entry_id    varchar(255),
    driver_category integer,
    first_name      varchar(255),
    last_name       varchar(255),
    player_id       varchar(255),
    short_name      varchar(255)
);

alter table driver
    owner to postgres;

create table event
(
    event_id                  varchar(255) not null
        primary key,
    ambient_temp              integer,
    cloud_level               double precision,
    event_name                varchar(255),
    meta_data                 varchar(255),
    post_qualy_time_sec       integer,
    post_race_time_sec        integer,
    pre_race_waiting_time_sec integer,
    rain                      double precision,
    session_over_time_sec     integer,
    track                     varchar(255),
    track_temp                integer,
    weather_randomness        integer
);

alter table event
    owner to postgres;

create table event_rules
(
    event_rules_id                            varchar(255) not null
        primary key,
    driver_stint_time_sec                     integer,
    is_mandatory_pitstop_refuelling_required  boolean,
    is_mandatory_pitstop_swap_driver_required boolean,
    is_mandatory_pitstop_tyre_change_required boolean,
    is_refuelling_allowed_in_race             boolean,
    is_refuelling_time_fixed                  boolean,
    mandatory_pitstop_count                   integer,
    max_drivers_count                         integer,
    max_total_driving_time                    integer,
    pit_window_length_sec                     integer,
    qualify_standing_type                     integer,
    superpole_max_car                         integer,
    tyre_set_count                            integer
);

alter table event_rules
    owner to postgres;

create table instances
(
    instance_id     varchar(255) not null
        primary key,
    assist_rules_id varchar(255),
    bop_id          varchar(255),
    config_id       varchar(255),
    container_image varchar(255),
    entries_id      varchar(255),
    event_id        varchar(255),
    event_rules_id  varchar(255),
    instance_name   varchar(255),
    settings_id     varchar(255)
);

alter table instances
    owner to postgres;

create table sessions
(
    session_id           varchar(255) not null
        primary key,
    day_of_weekend       integer,
    event_id             varchar(255),
    hour_of_day          integer,
    session_duration_min integer,
    session_type         varchar(255),
    time_multiplier      integer
);

alter table sessions
    owner to postgres;

create table settings
(
    settings_id                   varchar(255) not null
        primary key,
    admin_password                varchar(255),
    allow_auto_dq                 integer,
    car_group                     varchar(255),
    central_entry_list_path       varchar(255),
    config_version                integer,
    do_driver_swap_broadcast      integer,
    dump_entry_list               integer,
    dump_leader_boards            integer,
    formation_lap_type            integer,
    is_prep_phase_locked          integer,
    is_race_locked                integer,
    max_car_slots                 integer,
    race_craft_rating_requirement integer,
    randomize_track_when_empty    integer,
    safety_rating_requirement     integer,
    server_instance_name          varchar(255),
    server_password               varchar(255),
    short_formation_lap           integer,
    spectator_password            varchar(255),
    track_medals_requirement      integer
);

alter table settings
    owner to postgres;

create table users
(
    user_id                 integer not null
        primary key,
    account_non_expired     boolean,
    account_non_locked      boolean,
    credential_updated      timestamp,
    credentials_non_expired boolean,
    enabled                 boolean,
    password                varchar(255),
    username                varchar(255)
);

alter table users
    owner to postgres;

create table users_roles
(
    role_id   integer not null
        primary key,
    role_name varchar(255)
);

alter table users_roles
    owner to postgres;

create table users_authority
(
    authority_id integer not null
        primary key,
    permission   varchar(255),
    role_id      integer
        constraint fkt0fwkodjcnw0im8mjp4vtk8rf
            references users_roles
);

alter table users_authority
    owner to postgres;

create table users_roles_authorities
(
    user_id integer not null
        constraint fk4xb4ldkxod433g2lobw3o6yw4
            references users,
    role_id integer not null
        constraint fks4gj2qe9p8rm5x9l3yrsn8g08
            references users_roles,
    primary key (user_id, role_id)
);

alter table users_roles_authorities
    owner to postgres;

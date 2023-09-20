create table language (
    id tinyint not null,
    code varchar(255) not null,
    created_ts TIMESTAMP WITH TIME ZONE not null,
    modified_ts TIMESTAMP WITH TIME ZONE not null,
    name varchar(255) not null,
    primary key (id)
);

create table local_user (
    id bigint not null,
    created_ts TIMESTAMP WITH TIME ZONE not null,
    email varchar(255),
    is_enabled_flg boolean default true,
    external_id varchar(255) not null,
    modified_ts TIMESTAMP WITH TIME ZONE not null,
    password varchar(255),
    role varchar(255),
    username varchar(255),
    primary key (id)
);

create table study_state (
    id bigint not null,
    created_ts TIMESTAMP WITH TIME ZONE not null,
    modified_ts TIMESTAMP WITH TIME ZONE not null,
    is_passed_flg boolean default false not null,
    is_skipped_flg boolean default false not null,
    text_pair_id bigint not null,
    primary key (id)
);

create table text_pair (
    id bigint not null,
    created_ts TIMESTAMP WITH TIME ZONE not null,
    modified_ts TIMESTAMP WITH TIME ZONE not null,
    text1 varchar(255) not null,
    text2 varchar(255) not null,
    user_id bigint not null,
    primary key (id)
);

create table word (
    id bigint not null,
    created_ts TIMESTAMP WITH TIME ZONE not null,
    modified_ts TIMESTAMP WITH TIME ZONE not null,
    position bigint,
    text1 varchar(255) not null,
    language_id tinyint not null,
    text_pair_id bigint not null,
    primary key (id)
);

alter table if exists language 
    add constraint u_language_code unique (code);

alter table if exists local_user 
    add constraint UK_eavbyh8joues7ibdpx8kb3eqd unique (external_id);

create sequence local_user_seq start with 1 increment by 50;

create sequence study_state_seq start with 1 increment by 50;

create sequence text_pair_seq start with 1 increment by 50;

create sequence word_seq start with 1 increment by 50;

alter table if exists study_state 
    add constraint fk_study_state_text_pair 
    foreign key (text_pair_id) 
    references text_pair;

alter table if exists text_pair 
    add constraint fk_text_pair_user 
    foreign key (user_id) 
    references local_user;

alter table if exists word 
    add constraint fk_word_language 
    foreign key (language_id) 
    references language;

alter table if exists word 
    add constraint fk_word_text_pair 
    foreign key (text_pair_id) 
    references text_pair;

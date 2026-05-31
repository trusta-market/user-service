--liquibase formatted sql

--changeset seungwon:10
ALTER TABLE p_users DROP CONSTRAINT IF EXISTS uk_p_users_name;

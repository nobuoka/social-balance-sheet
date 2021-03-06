CREATE TABLE "users" (
  "id" BIGSERIAL PRIMARY KEY,
  "display_name" VARCHAR(128) NOT NULL
);

CREATE TABLE "login_sessions" (
  "id" BIGSERIAL PRIMARY KEY,
  "user_id" BIGINT NOT NULL
);

CREATE TABLE "twitter_users" (
  "twitter_user_id" BIGINT NOT NULL PRIMARY KEY,
  "twitter_user_name" VARCHAR(128) NOT NULL
);

CREATE TABLE "twitter_user_connections" (
  "user_id" BIGINT NOT NULL,
  "twitter_user_id" BIGINT NOT NULL
);

CREATE TABLE "twitter_temporary_credentials" (
  "identifier" VARCHAR(512) NOT NULL PRIMARY KEY,
  "shared_secret" VARCHAR(512) NOT NULL
);

CREATE TABLE "account_books" (
  "id" BIGSERIAL NOT NULL PRIMARY KEY,
  "label" VARCHAR(128) NOT NULL
);

CREATE TABLE "user_account_books" (
  "user_id" BIGINT NOT NULL,
  "account_book_id" BIGINT NOT NULL
);

CREATE TABLE "accounts" (
  "id" BIGSERIAL NOT NULL PRIMARY KEY,
  "account_book_id" BIGINT NOT NULL,
  "label" VARCHAR(128) NOT NULL
);

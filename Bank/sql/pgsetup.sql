-- Table: public.CLIENTS

-- DROP TABLE public."CLIENTS";

CREATE TABLE public."CLIENTS"
(
    "CLNT_ID" integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    "NAME" character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "PK_CLNT_ID" PRIMARY KEY ("CLNT_ID")
)

TABLESPACE pg_default;

ALTER TABLE public."CLIENTS"
    OWNER to postgres;

-- Table: public.CLIENT_BALANCE

-- DROP TABLE public."CLIENT_BALANCE";

CREATE TABLE public."CLIENT_BALANCE"
(
    "CLNT_CLNT_ID" integer NOT NULL,
    "BALANCE" numeric(12,2) NOT NULL,
    CONSTRAINT fk_clnt_clnt_id FOREIGN KEY ("CLNT_CLNT_ID")
        REFERENCES public."CLIENTS" ("CLNT_ID") MATCH SIMPLE
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
)

TABLESPACE pg_default;

ALTER TABLE public."CLIENT_BALANCE"
    OWNER to postgres;

-- Table: public.CLIENT_TRANSACTIONS

-- DROP TABLE public."CLIENT_TRANSACTIONS";

CREATE TABLE public."CLIENT_TRANSACTIONS"
(
    "CTRS_ID" integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    "CLNT_CLNT_ID" integer NOT NULL,
    "AMOUNT" numeric(12,2) NOT NULL,
    "BALANCE" numeric(12,2) NOT NULL,
    "ACTION_DATE" timestamp NOT NULL,
    CONSTRAINT fk_clnt_clnt_id FOREIGN KEY ("CLNT_CLNT_ID")
        REFERENCES public."CLIENTS" ("CLNT_ID") MATCH SIMPLE
        ON UPDATE RESTRICT
        ON DELETE RESTRICT
)

TABLESPACE pg_default;

ALTER TABLE public."CLIENT_TRANSACTIONS"
    OWNER to postgres;
	
INSERT INTO public."CLIENTS"("NAME") VALUES('Petya');
INSERT INTO public."CLIENT_BALANCE"("CLNT_CLNT_ID", "BALANCE") VALUES(1, 0);
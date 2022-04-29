CREATE TABLE public.products
(
    id serial NOT NULL,
    code character varying(10) NOT NULL,
    name character varying(512) NOT NULL,
    price_hrk numeric(19, 4) NOT NULL,
    price_eur numeric(19, 4) NOT NULL,
    description character varying(2048) NOT NULL,
    is_available boolean NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE public.products
    OWNER to postgres;

ALTER TABLE public.products
    ADD CONSTRAINT products_uq_code UNIQUE (code);

ALTER TABLE public.products
    ADD CONSTRAINT products_length_code CHECK (char_length(code) = 10);

ALTER TABLE public.products
    ADD CONSTRAINT products_amount_price_hrk CHECK (price_hrk >= 0);

ALTER TABLE public.products
    ADD CONSTRAINT products_amount_price_eur CHECK (price_eur >= 0);

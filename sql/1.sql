CREATE TABLE IF NOT EXISTS public.questions
(
    question_id SERIAL NOT NULL ,
    theme_id integer,
    caption text COLLATE pg_catalog."default" NOT NULL,
    url text COLLATE pg_catalog."default" NOT NULL DEFAULT 'null image'::text,
    count_of_button integer,
    correct_button integer,
    PRIMARY KEY (question_id)
);

CREATE TABLE IF NOT EXISTS public.theme
(
    theme_id SERIAL NOT NULL ,
    caption text COLLATE pg_catalog."default" NOT NULL,
	 PRIMARY KEY (theme_id)
);

ALTER TABLE IF EXISTS public.questions
    ADD FOREIGN KEY (theme_id)
    REFERENCES public.theme (theme_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;
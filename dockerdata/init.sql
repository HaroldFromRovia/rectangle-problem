CREATE SCHEMA rectangles;
drop extension postgis cascade;
create extension postgis with schema rectangles;
ALTER DATABASE "rectangle-problem-db" SET search_path=public,rectangles;
SELECT lrp.height * lrp.width * (1600 + lrp.rectangle_index) as product,
       lrp.rectangle_index
FROM rectangles.lrp
WHERE rectangle_index is not null;

SELECT 40000 * SUM(scrap.width * scrap.height)
FROM rectangles.scrap
         LEFT JOIN rectangle r on scrap.id = r.scrap_id
WHERE scrap_id is null;

SELECT 40000 * height * lrp.width
FROM rectangles.lrp
ORDER BY id DESC
LIMIT 1;

SELECT lrp.height * lrp.width * rectangle.index * 1600 as volume
FROM rectangles_40.lrp,
     rectangles_40.rectangle;
SELECT lrp.width, lrp.height
FROM rectangles_40.lrp;

SELECT height, width
FROM rectangles_40.rectangle;

SELECT scrap.height, scrap.width
FROM rectangles_40.scrap
WHERE not processed;

SELECT lrp.height, lrp.width, lrp.rectangle_index
FROM rectangles_10.lrp
ORDER BY rectangle_index;

SELECT COUNT(*)
FROM rectangles_40.scrap;
SELECT COUNT(*)
FROM rectangles_40.rectangle;

SELECT s.height, s.width
FROM "rectangles_10"."scrap" s
WHERE not processed
ORDER BY height;

select rectangle.index
from "rectangles_40".rectangle
ORDER BY index DESC
LIMIT 1;

SELECT scrap.id, scrap.figure
FROM "rectangles_10"."scrap"
         LEFT JOIN "rectangles_10".rectangle r on scrap.id = r.scrap_id
WHERE scrap.processed = false;

-- Торцевые
SELECT s.height, s.width
FROM "rectangles_10"."scrap" s
WHERE NOT processed
  AND is_end_face
ORDER BY height;

-- Прямоугольные
SELECT s.height, s.width
FROM "rectangles_10"."scrap" s
WHERE NOT processed
  AND is_rectangle
ORDER BY height;

-- Все обрезки
SELECT s.height * s.width
FROM "rectangles_10"."scrap" s
WHERE NOT processed;

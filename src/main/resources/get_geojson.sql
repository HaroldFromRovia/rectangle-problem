SELECT json_build_object(
               'type', 'FeatureCollection',
               'features', json_agg(
                       json_build_object(
                               'properties', '{}'::json,
                               'type', 'Feature',
                               'geometry', ST_AsGeoJSON((rectangle.figure)
                                   )::json))
           )
FROM rectangle;

select st_asgeojson(scrap)
FROM rectangles.scrap;

CREATE OR REPLACE FUNCTION update_post_likes()
RETURNS TRIGGER 
LANGUAGE plpgsql
AS $function$
DECLARE
    like_value INT;
BEGIN
    
    like_value := CASE WHEN NEW.Upvote = true THEN 1 ELSE -1 END;

    UPDATE Posts
    SET value_Of_Likes = value_Of_Likes + like_value
    WHERE id = NEW.post_id;
    RETURN NEW;
END;
$funcion$;

CREATE TRIGGER like_updated_trigger
AFTER INSERT ON Likes
FOR EACH ROW
EXECUTE FUNCTION update_post_likes();

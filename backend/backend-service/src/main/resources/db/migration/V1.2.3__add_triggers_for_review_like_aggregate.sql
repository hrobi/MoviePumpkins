CREATE FUNCTION update_review_like_aggregate_after_insert()
    RETURNS TRIGGER
AS
$$
BEGIN
    INSERT INTO app.review_like_aggregate (review_id, like_sum)
    VALUES (NEW.review_id, 1)
    ON CONFLICT (review_id)
        DO UPDATE SET like_sum = review_like_aggregate.like_sum + 1;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_review_like_after_insert
    AFTER INSERT
    ON review_like
    FOR EACH ROW
EXECUTE FUNCTION update_review_like_aggregate_after_insert();

CREATE FUNCTION update_review_like_aggregate_after_delete()
    RETURNS TRIGGER
AS
$$
BEGIN
    UPDATE app.review_like_aggregate
    SET like_sum = like_sum - 1
    WHERE review_id = OLD.review_id;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_review_like_after_delete
    AFTER DELETE
    ON review_like
    FOR EACH ROW
EXECUTE FUNCTION update_review_like_aggregate_after_delete();

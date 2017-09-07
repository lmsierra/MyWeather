package lmsierra.com.myweather;

import android.test.AndroidTestCase;

import lmsierra.com.myweather.model.Suggestion;
import lmsierra.com.myweather.model.db.dao.SuggestionDAO;

public class SuggestionDAOTest extends AndroidTestCase{

    private static final String DATABASE_TEST_NAME = "testmyweather.sqlite";
    private static final String SUGGESTION_TEST_TITLE = "test suggestion title";
    private static final String BEFORE_UPDATE = "before update";
    private static final String AFTER_UPDATE = "after update";


    public void testCanCreateNewRecentSearchDAO(){
        SuggestionDAO suggestionsDAO = new SuggestionDAO(getContext(), DATABASE_TEST_NAME);
        assertNotNull(suggestionsDAO);
    }

    public void testCanInsertRecentSuggestion(){
        SuggestionDAO suggestionDAO = new SuggestionDAO(getContext(), DATABASE_TEST_NAME);
        suggestionDAO.deleteAll();

        Suggestion suggestion = new Suggestion(SUGGESTION_TEST_TITLE);
        final long sut = suggestionDAO.insert(suggestion);

        assertTrue(sut >= 1);
    }

    public void testCanInsert10RecentSearches(){
        SuggestionDAO suggestionDAO = new SuggestionDAO(getContext(), DATABASE_TEST_NAME);
        suggestionDAO.deleteAll();

        final int beforeInsertRecords = suggestionDAO.queryCursor().getCount();

        insertTenSuggestions(suggestionDAO);

        final int afterInsertRecords = suggestionDAO.queryCursor().getCount();

        assertTrue(afterInsertRecords == beforeInsertRecords + 10);
    }

    private void insertTenSuggestions(SuggestionDAO suggestionDAO){

        for(int i = 0; i < 10; i++){
            Suggestion suggestion = new Suggestion(SUGGESTION_TEST_TITLE + i);
            suggestionDAO.insert(suggestion);
        }
    }

    public void testCanUpdateRecentSearch(){
        SuggestionDAO suggestionDAO = new SuggestionDAO(getContext(), DATABASE_TEST_NAME);
        Suggestion suggestion = new Suggestion(BEFORE_UPDATE);

        long id = suggestionDAO.insert(suggestion);

        Suggestion afterUpdateSuggestion = new Suggestion(AFTER_UPDATE);
        long numUpdatedRecords = suggestionDAO.update(id, afterUpdateSuggestion);

        assertEquals(1, numUpdatedRecords);

        Suggestion updatedSuggestion = suggestionDAO.query(id);

        assertEquals(AFTER_UPDATE, updatedSuggestion.getTitle());
    }

    public void testCanDeleteAllSearches(){
        SuggestionDAO suggestionDAO = new SuggestionDAO(getContext(), DATABASE_TEST_NAME);

        insertTenSuggestions(suggestionDAO);

        suggestionDAO.deleteAll();

        final int sut = suggestionDAO.queryCursor().getCount();
        assertEquals(0, sut);
    }

    public void testCanDeleteASearch(){
        SuggestionDAO suggestionDAO = new SuggestionDAO(getContext(), DATABASE_TEST_NAME);

        int beforeInsertAndDeleteRecords = suggestionDAO.query().count();

        Suggestion suggestion = new Suggestion(SUGGESTION_TEST_TITLE);

        long id = suggestionDAO.insert(suggestion);
        suggestionDAO.delete(id);

        int afterInsertAndDeleteRecords = suggestionDAO.query().count();

        assertEquals(beforeInsertAndDeleteRecords, afterInsertAndDeleteRecords);
    }
}

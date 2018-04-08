package com.badi.data.domain.interactor.search;

import com.badi.common.executor.JobExecutor;
import com.badi.common.executor.UIThread;
import com.badi.data.entity.room.Room;
import com.badi.data.entity.search.Filters;
import com.badi.data.entity.search.Location;
import com.badi.data.entity.search.SearchRooms;
import com.badi.data.repository.RoomDataRepository;
import com.badi.domain.interactor.search.SearchRoomsByLocation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.TestScheduler;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchRoomsByLocationTest {

    private static final String SORT_BY_PUBLISHED_AT = "published_at desc";
    private static final int PAGE_SIZE = 20;
    private static final int PAGINATED_PAGE = 2;
    private static final int PAGINATED_OFFSET = 20;

    private SearchRoomsByLocation useCase;
    private TestDisposableObserver<Object> testObserver;
    private List<Room> emptyRoomList = new ArrayList<>();

    @Mock private JobExecutor mockThreadExecutor;
    @Mock private UIThread mockPostExecutionThread;
    @Mock private Location mockLocation;
    @Mock private Filters mockFilters;
    @Mock private RoomDataRepository mockRoomDataRepository;

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        this.useCase = new SearchRoomsByLocation(mockRoomDataRepository, mockThreadExecutor, mockPostExecutionThread);
        this.testObserver = new TestDisposableObserver<>();

        SearchRooms searchRoomsRequest = getRegularSearchRooms();
        when(mockRoomDataRepository.getRoomsBySearch(eq(searchRoomsRequest))).thenReturn(Observable.just(emptyRoomList));

        SearchRooms searchRoomsRequestPaginated = getPaginatedSearchRooms();
        when(mockRoomDataRepository.getRoomsBySearch(eq(searchRoomsRequestPaginated))).thenReturn(Observable.just(emptyRoomList));

        given(mockPostExecutionThread.getScheduler()).willReturn(new TestScheduler());
    }

    @Test
    public void testBuildUseCaseObservableReturnCorrectResult() {
        useCase.execute(mockLocation, mockFilters, testObserver);

        assertEquals(0, testObserver.valuesCount);
    }

    @Test
    public void testBuildPaginatedUseCaseObservableReturnCorrectResult() {
        useCase.executePaginated(PAGINATED_PAGE, PAGINATED_OFFSET, testObserver);

        assertEquals(0, testObserver.valuesCount);
    }

    @Test
    public void testSubscriptionWhenExecutingUseCase() {
        useCase.execute(mockLocation, mockFilters, testObserver);
        useCase.dispose();

        assertTrue(testObserver.isDisposed());
    }

    @Test
    public void testShouldFailWhenExecuteWithNullObserver() {
        expectedException.expect(NullPointerException.class);
        useCase.execute(mockLocation, mockFilters, null);
    }

    private SearchRooms getPaginatedSearchRooms() {
        return SearchRooms.builder()
                .setLocation(null)
                .setPage(PAGINATED_PAGE)
                .setPer(PAGE_SIZE)
                .setOffset(PAGINATED_OFFSET)
                .setBedTypes(null)
                .setAmenities(null)
                .setMaxPrice(null)
                .setSortBy(SORT_BY_PUBLISHED_AT)
                .setNewSearchMode(true)
                .build();
    }

    private SearchRooms getRegularSearchRooms() {
        return SearchRooms.builder()
                .setLocation(mockLocation)
                .setPage(1)
                .setPer(PAGE_SIZE)
                .setOffset(0)
                .setBedTypes(new ArrayList<>())
                .setAmenities(new ArrayList<>())
                .setMaxPrice(0)
                .setSortBy(SORT_BY_PUBLISHED_AT)
                .setNewSearchMode(true)
                .build();
    }

    private static class TestDisposableObserver<T> extends DisposableObserver<T> {
        private int valuesCount = 0;

        @Override public void onNext(T value) {
            valuesCount++;
        }

        @Override public void onError(Throwable e) {
            // no-op by default.
        }

        @Override public void onComplete() {
            // no-op by default.
        }
    }
}
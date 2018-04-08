package com.badi.data.repository;

import com.badi.common.executor.JobExecutor;
import com.badi.common.executor.PostExecutionThread;
import com.badi.common.executor.ThreadExecutor;
import com.badi.common.executor.UIThread;
import com.badi.data.entity.room.Room;
import com.badi.data.entity.search.Filters;
import com.badi.data.entity.search.Location;
import com.badi.domain.interactor.search.SearchRoomsByLocation;
import com.badi.domain.repository.RoomRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.TestScheduler;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SearchRoomsByLocationTest {
    private SearchRoomsByLocation useCase;

    private TestDisposableObserver<Object> testObserver;

    @Mock private JobExecutor mockThreadExecutor;
    @Mock private UIThread mockPostExecutionThread;
    @Mock private RoomRepository roomRepository;
    @Mock private Location location;
    @Mock private Filters filters;

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        this.useCase = new SearchRoomsByLocation(roomRepository, mockThreadExecutor,mockPostExecutionThread);
        this.testObserver = new TestDisposableObserver<>();
        when(roomRepository.getRoomsBySearch(any())).thenReturn(null);
    }

    @Test
    public void testBuildUseCaseObservableReturnCorrectResult() {
        useCase.execute(location, filters, testObserver);

        assertEquals(0, testObserver.valuesCount);
    }

    @Test
    public void testSubscriptionWhenExecutingUseCase() {
        useCase.execute(location, filters, testObserver);
        useCase.dispose();

        assertTrue(testObserver.isDisposed());
    }

    @Test
    public void testShouldFailWhenExecuteWithNullObserver() {
        expectedException.expect(NullPointerException.class);
        //useCase.execute(null, Params.EMPTY);
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
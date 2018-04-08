package com.badi.presentation.search;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DialogNeedABadiTest {

    @InjectMocks private DialogNeedABadi dialogNeedABadi;

    @Mock DialogNeedABadi.OnDialogNeedABadiListener onDialogNeedABadiListener;

    @Before
    public void setUp() {
        dialogNeedABadi = new DialogNeedABadi();
        dialogNeedABadi.setOnDialogNeedABadiListener(onDialogNeedABadiListener);
    }

    @Test
    public void testOnClickCloseDismissDialog() {
        dialogNeedABadi.onClickClose();

        verify(onDialogNeedABadiListener, times(1)).onDismissDialog(any());
    }

    @Test
    public void testOnClickListRoomDismissDialogAndCallListener() {
        dialogNeedABadi.onClickListRoom();

        verify(onDialogNeedABadiListener, times(1)).onListRoomClicked();
        verify(onDialogNeedABadiListener, times(1)).onDismissDialog(any());
    }
}

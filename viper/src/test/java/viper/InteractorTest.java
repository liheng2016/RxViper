package viper;

import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static rx.Observable.just;

/**
 * ~ ~ ~ ~ Description ~ ~ ~ ~
 *
 * @author Dmitriy Zaitsev
 * @since 2016-May-14, 13:56
 */
public class InteractorTest {

  private static final int    PARAM  = 1;
  private static final String RESULT = "1";
  private Interactor<Integer, String> mInteractor;

  @Before public void setUp() {
    mInteractor = spy(new Interactor<Integer, String>(Schedulers.immediate(), Schedulers.immediate()) {
      @Override protected Observable<String> createObservable(final Integer integer) {
        return just(String.valueOf(integer));
      }
    });
  }

  @Test public void shouldCreateObservableSubscriber() {
    mInteractor.execute(new Subscriber<String>() {
      @Override public void onCompleted() {
      }

      @Override public void onError(final Throwable e) {
      }

      @Override public void onNext(final String s) {
      }
    });
    verify(mInteractor).createObservable(null);
  }

  @Test public void shouldCreateObservableSubscriberParam() {
    when(mInteractor.createObservable(PARAM)).thenReturn(just(RESULT));
    mInteractor.execute(new Subscriber<String>() {
      @Override public void onCompleted() {
      }

      @Override public void onError(final Throwable e) {
      }

      @Override public void onNext(final String s) {
      }
    }, PARAM);
    verify(mInteractor).createObservable(PARAM);
  }

  @Test public void shouldCreateObservableOnNext() {
    mInteractor.execute(new Action1<String>() {
      @Override public void call(final String s) {
      }
    });
    verify(mInteractor).createObservable(null);
  }

  @Test public void shouldCreateObservableOnNextParam() {
    when(mInteractor.createObservable(PARAM)).thenReturn(just(RESULT));
    mInteractor.execute(new Action1<String>() {
      @Override public void call(final String s) {
      }
    }, PARAM);
    verify(mInteractor).createObservable(PARAM);
  }

  @Test public void shouldCreateObservableOnNextOnError() {
    mInteractor.execute(new Action1<String>() {
      @Override public void call(final String s) {
      }
    }, new Action1<Throwable>() {
      @Override public void call(final Throwable t) {
      }
    });
    verify(mInteractor).createObservable(null);
  }

  @Test public void shouldCreateObservableOnNextOnErrorOnComplete() {
    mInteractor.execute(new Action1<String>() {
      @Override public void call(final String s) {
      }
    }, new Action1<Throwable>() {
      @Override public void call(final Throwable t) {
      }
    }, new Action0() {
      @Override public void call() {
      }
    });
    verify(mInteractor).createObservable(null);
  }

  @Test public void shouldCreateObservableOnNextOnErrorParam() {
    when(mInteractor.createObservable(PARAM)).thenReturn(just(RESULT));
    mInteractor.execute(new Action1<String>() {
      @Override public void call(final String s) {
      }
    }, new Action1<Throwable>() {
      @Override public void call(final Throwable t) {
      }
    }, PARAM);
    verify(mInteractor).createObservable(PARAM);
  }

  @Test public void shouldCreateObservableOnNextOnErrorOnCompleteParam() {
    when(mInteractor.createObservable(PARAM)).thenReturn(just(RESULT));
    mInteractor.execute(new Action1<String>() {
      @Override public void call(final String s) {
      }
    }, new Action1<Throwable>() {
      @Override public void call(final Throwable t) {
      }
    }, new Action0() {
      @Override public void call() {
      }
    }, PARAM);
    verify(mInteractor).createObservable(PARAM);
  }

  @Test public void shouldBeSubscribedOnStart() {
    assertThat(mInteractor.isUnsubscribed()).isFalse();
  }

  @Test public void shouldUnsubscribe() {
    mInteractor.unsubscribe();
    assertThat(mInteractor.isUnsubscribed()).isTrue();
    mInteractor.unsubscribe();
    assertThat(mInteractor.isUnsubscribed()).isTrue();
  }

  @Test(expected = IllegalArgumentException.class) //
  public void subscriberShouldNotBeNull() {
    //noinspection unchecked
    mInteractor.execute((Subscriber) null);
  }

  @Test(expected = IllegalArgumentException.class) //
  public void subscriberParamShouldNotBeNull() {
    //noinspection unchecked
    mInteractor.execute((Subscriber) null, PARAM);
  }

  @Test(expected = IllegalArgumentException.class) //
  public void onNextShouldNotBeNull() {
    //noinspection unchecked
    mInteractor.execute((Action1) null);
  }

  @Test(expected = IllegalArgumentException.class) //
  public void onNextParamShouldNotBeNull() {
    //noinspection unchecked
    mInteractor.execute((Action1) null, PARAM);
  }

  @Test(expected = IllegalArgumentException.class) //
  public void onErrorShouldNotBeNull() {
    //noinspection unchecked
    mInteractor.execute(new Action1<String>() {
      @Override public void call(final String s) {
      }
    }, (Action1) null);
  }

  @Test(expected = IllegalArgumentException.class) //
  public void onErrorParamShouldNotBeNull() {
    mInteractor.execute(new Action1<String>() {
      @Override public void call(final String s) {
      }
    }, null, PARAM);
  }

  @Test(expected = IllegalArgumentException.class) //
  public void onCompleteShouldNotBeNull() {
    mInteractor.execute(new Action1<String>() {
      @Override public void call(final String s) {
      }
    }, new Action1<Throwable>() {
      @Override public void call(final Throwable throwable) {
      }
    }, (Action0) null);
  }

  @Test(expected = IllegalArgumentException.class) //
  public void onCompleteParamShouldNotBeNull() {
    mInteractor.execute(new Action1<String>() {
      @Override public void call(final String s) {
      }
    }, new Action1<Throwable>() {
      @Override public void call(final Throwable throwable) {
      }
    }, null, PARAM);
  }

  @Test(expected = IllegalArgumentException.class) //
  public void subscribeOnShouldNotBeNull() {
    new Interactor<Object, Object>(null, Schedulers.immediate()) {
      @Override protected Observable<Object> createObservable(final Object o) {
        return null;
      }
    };
  }

  @Test(expected = IllegalArgumentException.class) //
  public void observeOnOnShouldNotBeNull() {
    new Interactor<Object, Object>(null, Schedulers.immediate()) {
      @Override protected Observable<Object> createObservable(final Object o) {
        return null;
      }
    };
  }
}
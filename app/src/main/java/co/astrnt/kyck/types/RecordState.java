package co.astrnt.kyck.types;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

import static co.astrnt.kyck.widget.RecordButtonView.STATE_ON_COUNTDOWN;
import static co.astrnt.kyck.widget.RecordButtonView.STATE_ON_FINISH;
import static co.astrnt.kyck.widget.RecordButtonView.STATE_ON_RECORD;
import static co.astrnt.kyck.widget.RecordButtonView.STATE_PRE_RECORD;

@StringDef({STATE_PRE_RECORD, STATE_ON_COUNTDOWN, STATE_ON_RECORD, STATE_ON_FINISH})
@Retention(RetentionPolicy.SOURCE)
public @interface RecordState {
}
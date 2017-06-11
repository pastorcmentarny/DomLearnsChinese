package dms.pastor.chinesegame.learning.sentencespattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.data.game.Player;
import dms.pastor.chinesegame.data.learning.patterns.Pattern;
import dms.pastor.chinesegame.utils.DomUtils;
import dms.pastor.chinesegame.utils.UIUtils;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz-9817065a/
 * Created 02/01/2013
 */
public final class PatternLesson extends Activity implements View.OnClickListener {

    private Pattern selectedPattern = null;

    private ToggleButton patternSwitch;
    private TableLayout sentenceTable;
    private TextView patternContent;

    private TextView lessonTitle;
    private TextView lessonContent;
    private TextView sentence1text, sentence2text, sentence3text, sentence4text, sentence5text;
    private TableRow sentence1row, sentence2row, sentence3row, sentence4row, sentence5row;

    @SuppressLint("CutPasteId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.pattern);
        sentenceTable = (TableLayout) findViewById(R.id.sentence_table);
        patternContent = (TextView) findViewById(R.id.pattern_content);

        lessonTitle = (TextView) findViewById(R.id.pattern_title);
        lessonContent = (TextView) findViewById(R.id.pattern_content);
        sentence1row = (TableRow) findViewById(R.id.sentence1row);
        sentence2row = (TableRow) findViewById(R.id.sentence2row);
        sentence3row = (TableRow) findViewById(R.id.sentence3row);
        sentence4row = (TableRow) findViewById(R.id.sentence4row);
        sentence5row = (TableRow) findViewById(R.id.sentence5row);
        sentence1text = (TextView) findViewById(R.id.sentence1text);
        sentence2text = (TextView) findViewById(R.id.sentence2text);
        sentence3text = (TextView) findViewById(R.id.sentence3text);
        sentence4text = (TextView) findViewById(R.id.sentence4text);
        sentence5text = (TextView) findViewById(R.id.sentence5text);
        patternSwitch = (ToggleButton) findViewById(R.id.pattern_switch);
        patternSwitch.setOnClickListener(this);

        Player player = Player.getPlayer();
        selectedPattern = player.getCurrentPattern();

        if (selectedPattern != null) {
            setupGUI();
        } else {
            UIUtils.displayError(this, "Pattern not found");
            finish();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pattern_switch:
                if (patternSwitch.isChecked()) {
                    sentenceTable.setVisibility(View.GONE);
                    patternContent.setVisibility(View.VISIBLE);
                } else {
                    sentenceTable.setVisibility(View.VISIBLE);
                    patternContent.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void setupGUI() {
        lessonTitle.setText(DomUtils.getUnknownWhenNullString(selectedPattern.getTitle()));
        lessonContent.setText(DomUtils.getUnknownWhenNullString(selectedPattern.getDescription()));
        setExample(selectedPattern.getExample1(), sentence1row, sentence1text);
        setExample(selectedPattern.getExample2(), sentence2row, sentence2text);
        setExample(selectedPattern.getExample3(), sentence3row, sentence3text);
        setExample(selectedPattern.getExample4(), sentence4row, sentence4text);
        setExample(selectedPattern.getExample5(), sentence5row, sentence5text);
    }

    private void setExample(String example, TableRow sentenceRow, TextView sentenceText) {
        if (!example.equalsIgnoreCase("none")) {
            sentenceRow.setVisibility(View.VISIBLE);
            sentenceText.setText(example);
        } else {
            sentenceRow.setVisibility(View.GONE);
        }
    }


}
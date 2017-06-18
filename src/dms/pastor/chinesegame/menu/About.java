package dms.pastor.chinesegame.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import dms.pastor.chinesegame.R;
import dms.pastor.chinesegame.learning.practice.SentencePracticeIntro;
import dms.pastor.chinesegame.utils.Utils;

import static dms.pastor.chinesegame.utils.DomUtils.displayWandToast;
import static dms.pastor.chinesegame.utils.DomUtils.isStringNotEmpty;
import static dms.pastor.chinesegame.utils.DomUtils.msg;
import static dms.pastor.chinesegame.utils.UIUtils.setTextColor;

/**
 * Author Dominik Symonowicz
 * WWW:	https://dominiksymonowicz.com/welcome
 * IT BLOG:	https://dominiksymonowicz.blogspot.co.uk
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: https://www.linkedin.com/in/dominik-symonowicz
 */
public final class About extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        TextView aboutContent = (TextView) findViewById(R.id.about_content);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String aboutType = extras.getString("TOPIC", "NO HELP");
            setTextColor(aboutContent, R.color.about, this);
            switch (aboutType) {
                case "ME":
                    aboutContent.setText(getResources().getString(R.string.about_me_text));
                    break;
                case "PROGRAM":
                    aboutContent.setText(getResources().getString(R.string.about_program_text));
                    break;
                case "THANKS":
                    aboutContent.setText(getResources().getString(R.string.about_thanks_text));
                    break;
                case "EULA":
                    aboutContent.setText(getResources().getString(R.string.about_eula_text));
                    break;
                default:
                    setTextColor(aboutContent, R.color.error, this);
                    aboutContent.setText(getResources().getString(R.string.noAbout));
                    break;
            }
        } else {
            setTextColor(aboutContent, R.color.error, this);
            aboutContent.setText(getResources().getString(R.string.noAbout));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.secretmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.garlic:
                gatewayToHeaven();
                return true;
            default:
                return false;
        }
    }

    private void gatewayToHeaven() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(getString(R.string.secret_title));
        alert.setMessage(getString(R.string.secret_msg));

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("OK", getOkDialog(input));
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                msg(getApplicationContext(), "good choice");
            }
        });
        alert.show();
    }

    @NonNull
    private DialogInterface.OnClickListener getOkDialog(final EditText input) {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String cmd = input.getText().toString();

                if (isStringNotEmpty(cmd)) {
                    if (cmd.equalsIgnoreCase("State")) {
                        //DomUtils.msg(getApplicationContext(), "Nothing in test atm.");
                        Intent ii = new Intent(getApplicationContext(), SentencePracticeIntro.class);
                        startActivity(ii);
                    } else {
                        displayWandToast(getApplicationContext(), About.this, Utils.getInfoFor(getApplicationContext(), cmd), true, false);
                    }
                } else {
                    msg(getApplicationContext(), "Entering into magical garlic-o-cheesecake world!");
                }
            }
        };
    }
}

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import static org.junit.Assert.*;

import android.app.Activity;
import android.app.FragmentManager;

import com.herald.ezherald.exercise.ExerciseActivity;

@Config(manifest = "../EzHerald/AndroidManifest.xml",emulateSdk = 18,reportSdk = 10)
//@Config()
@RunWith(RobolectricTestRunner.class)
public class A {
	@Test
	public void test1() throws Exception {
		Activity act = Robolectric.buildActivity(ExerciseActivity.class).create().get();
		FragmentManager mgr = act.getFragmentManager();
		assertNotNull(mgr);
		//ActivityController<ExerciseActivity> act = Robolectric.buildActivity(activityClass);
		//assertNotNull("act",act);
		//act.create().start().resume().get();
		
		//assertNotNull("here",null);
		//FragmentManager mgr = act.getSupportFragmentManager();
		//assertNotNull("mgr",mgr);
		//Fragment fragb = new FragmentB();
		//mgr.beginTransaction().add(fragb, null).commit();
		//startFragment(fragb);
		//assertNotNull("gragb",fragb);
	}
}

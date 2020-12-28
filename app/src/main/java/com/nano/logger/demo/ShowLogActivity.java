package com.nano.logger.demo;

import com.nano.logger.Logger;
import com.nano.logger.config.LoggerConfiguration;
import com.nano.logger.demo.R;
import com.nano.logger.demo.base.PrintingLogActivity;
import java.util.Map;
import java.util.Date;
import java.time.temporal.Temporal;
import java.util.Collection;

public class ShowLogActivity extends PrintingLogActivity {
	
	@Override
	public void initData() {
		Logger.iTag("Show Log", "%s\n%s", "Hello World.", "Click on the menu icon to print more.") ;
		mToolBar.setOnMenuItemClickListener(menuItem -> {
			switch(menuItem.getItemId()) {
				case R.id.menu_print_json :
					printJsonData() ;
					break ;
				case R.id.menu_print_xml : 
					printXmlData() ;
					break ;
				case R.id.menu_print_array : 
					printArrayData() ;
					break ;
				case R.id.menu_print_collection :
					printCollection() ;
					break ;
				case R.id.menu_clear_log : 
					clearLog() ;
					break ;
			}
			return true ;
		}) ;
	}
    
	private void printJsonData() {
		printJson("SongList", TestData.JSON_DATA) ;
	}
	
	private void printXmlData() {
		printXml("Config", TestData.XML_DATA) ;
	}
	
	private void printArrayData() {
		printData("Languages", TestData.LANGUAGES) ;
		printData("Random Array", TestData.randomIntArray(15, 20, 150)) ;
		printData("Random Two Diemnsions Array", TestData.randomMultiDimensionalIntArray(2, 15, 20, 150)) ;
		printData("Empty Array", TestData.randomMultiDimensionalIntArray(3, 0, 0, 0)) ;
	}
	
	private void printCollection(){
		printData("Collection", TestData.randomList()) ;
		printData("Collection", TestData.randomTreeMap()) ;
	}
	
	private void printJson(String tag, String json) {
		Logger.dJson(json) ;
		Logger.dJson(tag, json) ;
		
		Logger.vJson(json) ;
		Logger.vJson(tag, json) ;
		
		Logger.json(json) ;
		Logger.json(tag, json) ;

		Logger.wJson(json) ;
		Logger.wJson(tag, json) ;
		
		Logger.eJson(json) ;
		Logger.eJson(tag, json) ;
	}
	
	private void printXml(String tag, String xml) {
		Logger.dXml(xml) ;
		Logger.dXml(tag, xml) ;

		Logger.vXml(xml) ;
		Logger.vXml(tag, xml) ;

		Logger.xml(xml) ;
		Logger.xml(tag, xml) ;

		Logger.wXml(xml) ;
		Logger.wXml(tag, xml) ;

		Logger.eXml(xml) ;
		Logger.eXml(tag, xml) ;
	}
	
	
	private void printData(String tag, Object data) {
		Logger.d(data) ;
		Logger.dTag(tag, data) ;

		Logger.v(data) ;
		Logger.vTag(tag, data) ;

		Logger.i(data) ;
		Logger.iTag(tag, data) ;

		Logger.w(data) ;
		Logger.wTag(tag, data) ;

		Logger.e(data) ;
		Logger.eTag(tag, data) ;
	}
	
}

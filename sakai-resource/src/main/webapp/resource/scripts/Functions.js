/*******************************************************************************
ADL SCORM 2004 4th Edition MSCE 

The ADL SCORM 2004 4th Ed. MSCE is licensed under

Creative Commons Attribution-Noncommercial-Share Alike 3.0 United States.

 

The Advanced Distributed Learning Initiative allows you to:

  *  Share - to copy, distribute and transmit the work.

  *  Remix - to adapt the work. 

 

Under the following conditions:

  *  Attribution. You must attribute the work in the manner specified by the author or licensor (but not in any way that suggests that they endorse you or your use of the work).

  *  Noncommercial. You may not use this work for commercial purposes. 

  *  Share Alike. If you alter, transform, or build upon this work, you may distribute the resulting work only under the same or similar license to this one. 

 

For any reuse or distribution, you must make clear to others the license terms of this work. 

Any of the above conditions can be waived if you get permission from the ADL Initiative. 

Nothing in this license impairs or restricts the author's moral rights.

*******************************************************************************/
function Trim(s)
{
  // Remove leading spaces and carriage returns

  while ((s.substring(0,1) == ' ') || (s.substring(0,1) == '\n') || (s.substring(0,1) == '\r'))
  {
    s = s.substring(1,s.length);
  }

  // Remove trailing spaces and carriage returns

  while ((s.substring(s.length-1,s.length) == ' ') || (s.substring(s.length-1,s.length) == '\n') || (s.substring(s.length-1,s.length) == '\r'))
  {
    s = s.substring(0,s.length-1);
  }
  return s;
}

 //-- BEGIN ASSESSMENT VALIDATION and FEEDBACK FUNCTIONS
 // Set some variables global to the SCO
   var answer = "";
   var rawScore = 0;
   var exitPageStatus = false;

   /**********************************************************************
   **  Function: submitenter( myfield, e )
   **  Description: This function is responsible for catching if the
   **  "Enter" key has been pressed in answering a fill-in question
   **  to submit the answer for validation, handled generically by
   **  checking for the event ("e").
   **********************************************************************/
   function submitenter( e )
   {
      var keycode;
      if ( window.event ) keycode = window.event.keyCode;
      else if ( e ) keycode = e.which;
      else return true;
      if ( keycode == 13 )
      {
         calcScore();
         return false;
      }
      else return true;
   }

   /**********************************************************************
   **  Function: convertCase()
   **  Description: This function is responsible lowercasing the user
   **               entered value.
   **********************************************************************/
   function convertCase()
   {
      var x = document.examForm.Q1.value;
      if ( x != "" )
      {
         x = Trim( x.toLowerCase() );
      }
      return x;
   }

   /**********************************************************************
   **  Function: calcRawScore()
   **  Description: This function is responsible for incrementing the raw
   **               score (cmi.score.raw) if the question was
   **               answered correctly.  If not, it keeps the raw score
   **               at its initial value (0).
   **********************************************************************/
   function calcRawScore()
   {
		rawScore = 0;
		//loop through the possible
		for( i=0; i < key.length; i++ )
		{
			if( convertCase() == Trim( key[i].toLowerCase() ) )
			{
				rawScore++;
				break;
			}
		}
   }

   /**********************************************************************
   **  Function: calcScore()
   **  Description: This function is responsible for using the raw score
   **               set in "calcRawScore()" to determine the status of
   **               the SCO.  The scaled score
   **               (cmi.score.scaled), Success Status
   **               (cmi.success_status) and Completion Status
   **               (cmi.completion_status) are set appropriately.
   **               After each value is set, the SCO is finished with a
   **               call to Terminate().
   **********************************************************************/
   function calcScore()
   {
      // Disable the submit buttons so that it can not be clicked
      //document.getElementById('submitBtn').visible = false;

      document.examForm.submitButton.disabled = true;

      // Get the raw score
      calcRawScore();

      //  Set the scaled score that will be used in the
      //  Sequencing Tracking Model
      doSetValue( "cmi.score.scaled", rawScore );

      //  Indicate whether or not the attempt on the SCO was
      //  sucessful (passed or failed)
      if ( rawScore != 1 )
      {
         doSetValue( "cmi.success_status", "failed" );
		 showDiv( "incorrect" );
      }
      else
      {
         doSetValue( "cmi.success_status", "passed" );
		 showDiv( "correct" );
      }

      //  Set the SCO to completed with a normal exit
      doSetValue( "cmi.completion_status", "completed" );
      doSetValue( "cmi.exit", "" );

      //  Indicate that the SCO was finished normally
      exitPageStatus = true;


   }

   /**********************************************************************
   **  Function: showDiv( exempt )
   **  Description: This function is called to display or hide content
   **  within a div tag identified by the parameter, "exempt."
   **********************************************************************/
   function pretestShowDiv( exempt )
   {
   	// if older browsers can't understand a document.getElementsByTagName
   	if (!document.getElementsByTagName)
	{
      	return null;
   	}
   	// otherwise, proceed...
   	var divs = document.getElementsByTagName("div");
   	for ( var i = 0; i < divs.length; i++ )
   	{
   		var div = divs[i];
   		var id = div.id;
   		if ( id.substring(0,6) == "assess" )
   		{
   			div.style.display = "none";
   		}
   		if ( id == exempt )
   		{
   			div.style.display = "block";
   		}

   	}
   }

   function pretestBtnAction(thisId, btn)
   {
      if (btn == 'prev')
	  {
		// Go to previous SCO if back is hit on first page
		if (prevId == 1){
		    Previous();
		    return;
		 }
		 else{
			 prevId--;
			 nextId--;
			 pretestShowDiv('assess'+prevId);
		}
      }
	  else
	  {
         prevId++;
         nextId++;
         pretestShowDiv('assess'+nextId);
      }
      document.getElementById('prevBtn').style.visibility='visible';
      document.getElementById('nextBtn').style.visibility='visible';
      document.getElementById('submitBtn').style.visibility='hidden';
      if (prevId == 1)
	  {
		  // On first page, display previous button if LMS indicates previous sco availible
		  if (RenderPreviousButton()) {
			  document.getElementById('prevBtn').style.visibility='visible';
		  }
		  else{
			  document.getElementById('prevBtn').style.visibility='hidden';
		  }
      }
      if (nextId == document.getElementById('content').getElementsByTagName("div").length)
	  {
         document.getElementById('nextBtn').style.visibility='hidden';
         document.getElementById('submitBtn').style.visibility='visible';
      }
   }




   /**********************************************************************
   **  Function: pretestCalcRawScore()
   **  Description: This function is responsible for incrementing the raw
   **               score (cmi.score.raw) if the question was
   **               answered correctly.  If not, it keeps the raw score
   **               at its initial value (0).
   **********************************************************************/
	function pretestCalcRawScore()
	{
		rawScore1 = 0;
		rawScore2 = 0;
		rawScore3 = 0;
		//loop through the possible
		for( i=0; i < key.length; i++ )
		{
			compareAnswer = "unanswered";
			if (i == "0"){compareAnswer = document.examForm.Q1.value;}
                        else if (i == "1"){for (var k=0; k<document.examForm.Q2.length; k++){if (document.examForm.Q2[k].checked) {compareAnswer = document.examForm.Q2[k].value;}}}
			else if (i == "2"){for (var k=0; k<document.examForm.Q3.length; k++){if (document.examForm.Q3[k].checked) {compareAnswer = document.examForm.Q3[k].value;}}}
			else if (i == "3"){for (var k=0; k<document.examForm.Q4.length; k++){if (document.examForm.Q4[k].checked) {compareAnswer = document.examForm.Q4[k].value;}}}
			else if (i == "4"){for (var k=0; k<document.examForm.Q5.length; k++){if (document.examForm.Q5[k].checked) {compareAnswer = document.examForm.Q5[k].value;}}}
			else if (i == "5"){compareAnswer = document.examForm.Q6.value;}
			else if (i == "6"){compareAnswer = document.examForm.Q7.value;}
			else if (i == "7"){compareAnswer = document.examForm.Q8.value;}
			else if (i == "8"){for (var k=0; k<document.examForm.Q9.length; k++){if (document.examForm.Q9[k].checked) {compareAnswer = document.examForm.Q9[k].value;}}}
			else if (i == "9"){compareAnswer = document.examForm.Q10.value;}
			else if (i == "10"){compareAnswer = document.examForm.Q11.value;}
			else if (i == "11"){compareAnswer = document.examForm.Q12.value;}
			else if (i == "12"){compareAnswer = document.examForm.Q13.value;}
			else if (i == "13"){compareAnswer = document.examForm.Q14.value;}
			else if (i == "14"){compareAnswer = document.examForm.Q15.value;}
			else if (i == "15"){compareAnswer = document.examForm.Q16.value;}
			else if (i == "16"){for (var k=0; k<document.examForm.Q17.length; k++){if (document.examForm.Q17[k].checked) {compareAnswer = document.examForm.Q17[k].value;}}}
			else if (i == "17"){for (var k=0; k<document.examForm.Q18.length; k++){if (document.examForm.Q18[k].checked) {compareAnswer = document.examForm.Q18[k].value;}}}
			else if (i == "18"){for (var k=0; k<document.examForm.Q19.length; k++){if (document.examForm.Q19[k].checked) {compareAnswer = document.examForm.Q19[k].value;}}}
			else if (i == "19"){for (var k=0; k<document.examForm.Q20.length; k++){if (document.examForm.Q20[k].checked) {compareAnswer = document.examForm.Q20[k].value;}}}
			else if (i == "20"){compareAnswer = document.examForm.Q21.value;}
			else if (i == "21"){for (var k=0; k<document.examForm.Q22.length; k++){if (document.examForm.Q22[k].checked) {compareAnswer = document.examForm.Q22[k].value;}}}
			else if (i == "22"){for (var k=0; k<document.examForm.Q23.length; k++){if (document.examForm.Q23[k].checked) {compareAnswer = document.examForm.Q23[k].value;}}}
			else if (i == "23"){for (var k=0; k<document.examForm.Q24.length; k++){if (document.examForm.Q24[k].checked) {compareAnswer = document.examForm.Q24[k].value;}}}
			else if (i == "24"){for (var k=0; k<document.examForm.Q25.length; k++){if (document.examForm.Q25[k].checked) {compareAnswer = document.examForm.Q25[k].value;}}}
			else if (i == "25"){for (var k=0; k<document.examForm.Q26.length; k++){if (document.examForm.Q26[k].checked) {compareAnswer = document.examForm.Q26[k].value;}}}
			else if (i == "26"){for (var k=0; k<document.examForm.Q27.length; k++){if (document.examForm.Q27[k].checked) {compareAnswer = document.examForm.Q27[k].value;}}}
			else if (i == "27"){for (var k=0; k<document.examForm.Q28.length; k++){if (document.examForm.Q28[k].checked) {compareAnswer = document.examForm.Q28[k].value;}}}
			else if (i == "28"){for (var k=0; k<document.examForm.Q29.length; k++){if (document.examForm.Q29[k].checked) {compareAnswer = document.examForm.Q29[k].value;}}}
			else if (i == "29"){for (var k=0; k<document.examForm.Q30.length; k++){if (document.examForm.Q30[k].checked) {compareAnswer = document.examForm.Q30[k].value;}}}
			else if (i == "30"){for (var k=0; k<document.examForm.Q31.length; k++){if (document.examForm.Q31[k].checked) {compareAnswer = document.examForm.Q31[k].value;}}}
			else if (i == "31"){for (var k=0; k<document.examForm.Q32.length; k++){if (document.examForm.Q32[k].checked) {compareAnswer = document.examForm.Q32[k].value;}}}
			else if (i == "32"){for (var k=0; k<document.examForm.Q33.length; k++){if (document.examForm.Q33[k].checked) {compareAnswer = document.examForm.Q33[k].value;}}}
			else if (i == "33"){for (var k=0; k<document.examForm.Q34.length; k++){if (document.examForm.Q34[k].checked) {compareAnswer = document.examForm.Q34[k].value;}}}
			else if (i == "34"){for (var k=0; k<document.examForm.Q35.length; k++){if (document.examForm.Q35[k].checked) {compareAnswer = document.examForm.Q35[k].value;}}}
			compareAnswer = Trim(compareAnswer.toLowerCase());
			if (typeof key[i] == "object")
			{
				for ( k=0; k < key[i].length; k++ )
				{
					compareKey = Trim( key[i][k].toLowerCase());
					alert (compareAnswer);
					alert (compareKey);
					if( compareAnswer == compareKey )
					{
						if (i <= 3){ rawScore1++; }
						if ( 3 < i && i <= 23){ rawScore2++; }
						if ( 23 < i && i <= 33){ rawScore3++; }
					}
				}
			}
			else
			{
				compareKey = Trim( key[i].toLowerCase());
				alert (compareAnswer);
				alert (compareKey);
				if( compareAnswer == compareKey )
				{
					if (i <= 3){ rawScore1++; }
					if ( 3 < i && i <= 23){ rawScore2++; }
					if ( 23 < i && i <= 33){ rawScore3++; }
				}
			}
		}
	}

   /**********************************************************************
   **  Function: calcScore()
   **  Description: This function is responsible for using the raw score
   **               set in "calcRawScore()" to determine the status of
   **               the SCO.  The scaled score
   **               (cmi.score.scaled), Success Status
   **               (cmi.success_status) and Completion Status
   **               (cmi.completion_status) are set appropriately.
   **               After each value is set, the SCO is finished with a
   **               call to Terminate().
   **********************************************************************/
   function pretestCalcScore()
   {
      // Disable the submit buttons so that it can not be clicked
      document.examForm.submitBtn.disabled = true;

      // Get the raw score
      pretestCalcRawScore();

      //  Set the scaled score that will be used in the
      //  Sequencing Tracking Model
      var childCount = doGetValue("cmi.objectives._count");

      for (i=0; i < childCount; i++){

         var currentVal = doGetValue("cmi.objectives."+i+".id");
         if (currentVal == "obj_module_1")
		 {
            var id1 = "cmi.objectives."+i;
         }
		 else if (currentVal == "obj_module_2")
		 {
            var id2 = "cmi.objectives."+i;
         }
		 else if (currentVal == "obj_module_3")
		 {
            var id3 = "cmi.objectives."+i;
         }
      }

	  var scaledScore1 = Math.round( rawScore1 / 3 * 100 ) / 100;
	  if (scaledScore1 >= 1 ) { scaledScore1 = 1; }
	  doSetValue( id1 + ".score.scaled", String( scaledScore1 ) );
      if ( rawScore1 >= 3 )
      {
         doSetValue( id1+".success_status", "passed" );
      }
      else
      {
         doSetValue( id1+".success_status", "failed" );
      }

 	  var scaledScore2 = Math.round( rawScore2 / 15 * 100 ) / 100;
  	  if (scaledScore2 >= 1 ) { scaledScore2 = 1; }
	  doSetValue( id2 + ".score.scaled", String( scaledScore2 ) );
      if ( rawScore2 >= 15 )
      {
         doSetValue( id2+".success_status", "passed" );
      }
      else
      {
         doSetValue( id2+".success_status", "failed" );
      }

	  var scaledScore3 = Math.round( rawScore3 / 8 * 100 ) / 100;
	  if (scaledScore3 >= 1 ) { scaledScore3 = 1; }
	  doSetValue( id3 + ".score.scaled", String( scaledScore3 ) );
	  if ( rawScore3 >= 8 )
      {
         doSetValue( id3+".success_status", "passed" );
      }
      else
      {
         doSetValue( id3+".success_status", "failed" );
      }
      //  Set the SCO to completed with a normal exit
      doSetValue( "cmi.completion_status", "completed" );
      doSetValue( "cmi.exit", "" );


      //  Indicate that the SCO was finished normally
      exitPageStatus = true;
	  IntraNavigation = true;
	  // we request the next SCO from the LMS
  	  doSetValue("adl.nav.request", "continue");

      var result = doTerminate();
   }

   /**********************************************************************
   **  Function: loadPage()
   **  Description: This is called when a SCO is first loaded in the
   **               browser (onload()).  It finds the API if it was not
   **               already located and calls Initialize().  In
   **               the exitPageStatus global variable is set to false
   **               indicating that the SCO is not yet finished.
   **********************************************************************/
   function loadPage()
   {
      Initialize();
	  SetContinue();
      exitPageStatus = false;
	  showDiv( "question" );
   }

   /**********************************************************************
   **  Function: doQuit()
   **  Description: This function is called in the case that the user
   **               does not finish the SCO "gracefully".  For example,
   **               the user may click the "continue" button before
   **               submitting an answer to a question.  In this case,
   **               this function is called as part of the page unloading.
   **               This function ensures that Terminate() is called
   **               and that the correct statuses are set even if the
   **               user closes the SCO window or navigates away before
   **               finishing the SCO.
   **********************************************************************/
   function doQuit()
   {
      calcScore();
   }

   /**********************************************************************
   **  Function: doQuit()
   **  Description: This function is called in the case that the user
   **               does not finish the SCO "gracefully".  For example,
   **               the user may click the "continue" button before
   **               submitting an answer to a question.  In this case,
   **               this function is called as part of the page unloading.
   **               This function ensures that Terminate() is called
   **               even if the user closes the SCO window or navigates
   **               away before finishing the SCO.
   **********************************************************************/
   function unloadPage()
   {

   	if (exitPageStatus != true)
   	{
   		doQuit();
   	}

   	// NOTE: don't return anything that resembles a javascript
   	//		   string from this function or IE will take the
   	//		   liberty of displaying a confirm message box
   }

   /**********************************************************************
   **  Function: showDiv( exempt )
   **  Description: This function is called to display or hide content
   **  within a div tag identified by the parameter, "exempt."
   **********************************************************************/
	function showDiv( exempt )
	{
		// if older browsers can't understand a document.getElementsByTagName
		if (!document.getElementsByTagName) {
			return null;
		}
		// otherwise, proceed...
		var divs = document.getElementsByTagName("div");
		for ( var i = 0; i < divs.length; i++ )
		{
			var div = divs[i];
			var id = div.id;
			if ( ( id == "question" ) || ( id == "correct" ) || ( id == "incorrect" ) )
			{
				div.style.display = "none";
			}
			if ( ( id == exempt ) )
			{
				div.style.display = "block";
			}

		}
	}
// JavaScript Document

// Global variables
var IntraNavigation = false

/*******************************************************************************
**
** This function is used to go to another page of a multi-page SCO
**
** Inputs:  page - the location that we're bookmarking
**
**
*******************************************************************************/
function GoToPage( page )
{
	IntraNavigation = true;

	// replace the current page with the page specified
	window.location.replace( page );
}

/*******************************************************************************
**
** This function is used to go to a previous SCO
**
*******************************************************************************/
function Previous() {
	// we request the previous SCO from the LMS
	doSetValue( "adl.nav.request", "previous" );
	var value = doGetValue("adl.nav.request_valid.previous");
	IntraNavigation = true;
	// we terminate this SCO's communication with the LMS
	doTerminate();
}

/*******************************************************************************
**
** This function is used to go to a next SCO
**
*******************************************************************************/
function Continue()
{
	// we request the next SCO from the LMS
	doSetValue("adl.nav.request", "continue");
	IntraNavigation = true;
	// we terminate this SCO's communication with the LMS
	doTerminate();
}
function PreviousQuiz() {
	// we request the previous SCO from the LMS
	doSetValue( "adl.nav.request", "previous" );
	var value = doGetValue("adl.nav.request_valid.previous");
	IntraNavigation = true;
	// we terminate this SCO's communication with the LMS
	var result = doTerminate();
}

/*******************************************************************************
**
** This function is used to go to a next SCO
**
*******************************************************************************/
function ContinueQuiz()
{
	// we request the next SCO from the LMS
	doSetValue("adl.nav.request", "continue");
	IntraNavigation = true;
	// we terminate this SCO's communication with the LMS
	var result = doTerminate();
}


function onUnexpectedExit()
{

	// we're going to check to see if this is a "good" exit or a "bad" exit
	if ( IntraNavigation == false )
	{
		// terminate our communication with the LMS
		doTerminate();
	}
	else
	{
		IntraNavigation = false;
	}
}


/*******************************************************************************
**
** Initialize SCO and set previous and continue button visible states
**
** Inputs:  String - "singlePage" triggers check for a continue sco
**                   otherwise only checks previous
**
** Return:  None
**
*******************************************************************************/

function Initialize(SCOLength)
{
	if ( !(entryStatus == "resume") )
	{
   		doInitialize();
	}

	// search dom for previous button - visibilty set off in html
	// turn previous button on if lms reports previous sco
	var imgs = document.getElementsByTagName("img");

	for ( var i = 0; i < imgs.length; i++ )	{
		var img = imgs[i];
		var className = img.className;

		if ( className == "prevBtn" ){
			if ( RenderPreviousButton() ){
				// show previous button
				img.style.visibility = "visible";
			}
		}
		if (SCOLength == "singlepage"){
			if ( className == "nextBtn" ){
				if ( RenderContinueButton() ){
					// show continue button
					img.style.visibility = "visible";
				}
			}
		}
	}

	// we need to determine if this is a new "learner attempt" or a
   	// suspended "learner attempt
   	var entryStatus = doGetValue( "cmi.entry" );


	// check to see if this a resumption of a suspended learner attempt
	/***********************************************************
	** Currently NOT needed (keep for future use/reminder)
	************************************************************
	if ( entryStatus == "resume" )
	{
		var location = retrieveDataValue( "cmi.location" );

		// jump to the location we just retrieved

			//find the path name of the current SCO
			var path = getSCOLocation(currentSCO);  //Need to recreate getSCOLocation function with method you decided to use
			var newLocation = path+location+".html";
			window.location.replace( newLocation );
	}
	*************************************************/
}

/*******************************************************************************
**
** This function is used to by multi-page SCOs in the process of determining
** whether or not to display a next button on the last page of a multi-page SCO
**
** Inputs:  None
**
** Return:  None
**
*******************************************************************************/

function SetContinue() {
	// search dom for next button - visibilty set off in html
	// turn next button on if lms reports continue sco
	var imgs = document.getElementsByTagName("img");

	for ( var i = 0; i < imgs.length; i++ )	{
		var img = imgs[i];
		var className = img.className;

		if ( className == "nextBtn" ){
			if ( RenderContinueButton() ){
				// show continue button
				img.style.visibility = "visible";
			}
		}
	}
}


/*******************************************************************************
**
** Makes the appropriate calls for a normal exit calling Terminate and
** setting some data model elements for a normal exit
**
** Inputs:  None
**
** Return:  None
**
*******************************************************************************/
function Terminate()
{
   if (!IntraNavigation)
   {
      doSetValue( "cmi.completion_status", "completed" );

      doSetValue( "cmi.exit", "" );

      doTerminate();
   }
}

/*******************************************************************************
**
** This function asks the LMS if there exists a previous SCO or Asset to go to.
** If a SCO or Asset exists, then the previous button is displayed.
**
** Inputs:  None
**
** Return:  String - "true" if the previous button should be displayed
**                   "false" if failed.
**
*******************************************************************************/
function RenderPreviousButton() {
	var value = doGetValue("adl.nav.request_valid.previous");

	if (value == "true"){
		return true;
	}
	return false;
}

/*******************************************************************************
**
** This function asks the LMS if there exists a next SCO or Asset to continue
** to.  If a SCO or Asset exists, then the continue button is displayed.
**
** Inputs:  None
**
** Return:  String - "true" if the continue button should be displayed
**                   "false" if failed.
**
*******************************************************************************/
function RenderContinueButton() {
	var value = doGetValue("adl.nav.request_valid.continue");

	if (value == "true"){
		return true;
	}
	return false;
}
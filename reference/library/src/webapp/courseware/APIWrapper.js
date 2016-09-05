function doInitialize(){var a=getAPIHandle();if(null==a)return alert("Unable to locate the LMS's API Implementation.\nInitialize was not successful."),"false";var b=a.Initialize("");return"true"!=b.toString()&&ErrorHandler(),b.toString()}function doTerminate(){var a=getAPIHandle();if(null==a)return alert("Unable to locate the LMS's API Implementation.\nTerminate was not successful."),"false";var b=a.Terminate("");return"true"!=b.toString()&&ErrorHandler(),b.toString()}function doGetValue(a){var b=getAPIHandle();if(null==b)return alert("Unable to locate the LMS's API Implementation.\nGetValue was not successful."),"";var c=b.GetValue(a),d=b.GetLastError().toString();if(d!=_NoError){var e=b.GetErrorString(d);return alert("GetValue("+a+") failed. \n"+e),""}return c.toString()}function doSetValue(a,b){var c=getAPIHandle();if(null==c)return alert("Unable to locate the LMS's API Implementation.\nSetValue was not successful."),void 0;var d=c.SetValue(a,b);"true"!=d.toString()&&ErrorHandler()}function doCommit(){var a=getAPIHandle();if(null==a)return alert("Unable to locate the LMS's API Implementation.\nCommit was not successful."),"false";var b=a.Commit("");return"true"!=b&&ErrorHandler(),b.toString()}function doGetLastError(){var a=getAPIHandle();return null==a?(alert("Unable to locate the LMS's API Implementation.\nGetLastError was not successful."),_GeneralError):a.GetLastError().toString()}function doGetErrorString(a){var b=getAPIHandle();return null==b&&alert("Unable to locate the LMS's API Implementation.\nGetErrorString was not successful."),b.GetErrorString(a).toString()}function doGetDiagnostic(a){var b=getAPIHandle();return null==b&&alert("Unable to locate the LMS's API Implementation.\nGetDiagnostic was not successful."),b.GetDiagnostic(a).toString()}function ErrorHandler(){var a=getAPIHandle();if(null==a)return alert("Unable to locate the LMS's API Implementation.\nCannot determine LMS error code."),void 0;var b=a.GetLastError().toString();if(b!=_NoError&&b!=_AlreadyInitialized){var c=a.GetErrorString(b);1==_Debug&&(c+="\n",c+=a.GetDiagnostic(null)),alert(c)}return b}function getAPIHandle(){return null==apiHandle&&(apiHandle=getAPI()),apiHandle}function findAPI(a){for(;null==a.API_1484_11&&null!=a.parent&&a.parent!=a;){if(findAPITries++,findAPITries>500)return alert("Error finding API -- too deeply nested."),null;a=a.parent}return a.API_1484_11}function getAPI(){var a=findAPI(window);return null==a&&null!=window.opener&&"undefined"!=typeof window.opener&&(a=findAPI(window.opener)),null==a&&alert("Unable to find an API adapter"),a}var _Debug=!1,_NoError=0,_GeneralException=101,_GeneralInitializationFailure=102,_AlreadyInitialized=103,_ContentInstanceTerminated=104,_GeneralTerminationFailure=111,_TerminationBeforeInitialization=112,_TerminationAfterTermination=113,_ReceivedDataBeforeInitialization=122,_ReceivedDataAfterTermination=123,_StoreDataBeforeInitialization=132,_StoreDataAfterTermination=133,_CommitBeforeInitialization=142,_CommitAfterTermination=143,_GeneralArgumentError=201,_GeneralGetFailure=301,_GeneralSetFailure=351,_GeneralCommitFailure=391,_UndefinedDataModelElement=401,_UnimplementedDataModelElement=402,_DataModelElementValueNotInitialized=403,_DataModelElementIsReadOnly=404,_DataModelElementIsWriteOnly=405,_DataModelElementTypeMismatch=406,_DataModelElementValueOutOfRange=407,apiHandle=null,API=null,findAPITries=0;
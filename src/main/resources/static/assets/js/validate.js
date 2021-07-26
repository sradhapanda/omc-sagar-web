<!-- Nirmalya Labs Validation javascript File on 14January2019 -->

<!-- ##################Function to check Textfield having no value ################# -->
function blankValidation(fieldId,fieldType, message){
    <!-- =====================For Textfield blankValidation================== -->
    if(fieldType == "TextField"){
      if ($('#' + fieldId).val() == ''){
        $('#' + fieldId).focus();
        swal(message);
        return false;
      }
      return true;
    }

    <!-- =====================For TextArea blankValidation================== -->
    if(fieldType == "TextArea"){
      if ($('#' + fieldId).val() == ''){
        $('#' + fieldId).focus();
        swal(message);
        return false;
      }
      return true;
    }

    <!-- =====================For Dropdown blankValidation================== -->
    if(fieldType == "SelectBox"){
      if ($('#' + fieldId).val() == '' || $('#' + fieldId).val() == '0'){
        $('#' + fieldId).focus();
        swal(message);
        return false;
      }
      return true;
    }
    <!-- =====================For Dropdown blankValidation================== -->
    if(fieldType == "RadioButton"){
      if ($('input[name="' + fieldId + '"]:checked').length <= 0){
          $('input[name=' + fieldId + ']:first').focus();
          swal(message);
          return false;
      }
      return true;
    }

    <!-- =====================For Checkbox blankValidation================== -->
    if(fieldType == "CheckBox"){
      if ($('input[name="' + fieldId + '"]:checked').length <= 0){
          $('input[name=' + fieldId + ']:first').focus();
          swal(message);
          return false;
      }
      return true;
    }

}

<!-- #################Function to Check the character length of a field ####################### -->
function fieldLengthCheck(fieldId, fieldLen, fieldName,checkType){
  <!-- ===============Function to Check the maxlength character of a field================ -->
  if(checkType == "Max"){
    if ($('#' + fieldId).val().length > fieldLen && $('#' + fieldId).val().length > 0){
        $('#' + fieldId).focus();
        swal(fieldName + ' cannot more than ' + fieldLen + ' charater !!!');
        return false;
    }
    return true;
  }

  <!-- ===============Function to Check the minlength character of a field================ -->
  if(checkType == "Min"){
    if ($('#' + fieldId).val().length < fieldLen && $('#' + fieldId).val().length > 0){
      $('#' + fieldId).focus();
      swal(fieldName + ' cannot less than ' + fieldLen + ' characters !!!');
      return false;
    }
    return true;
  }

  <!-- ===============Function to Check the equality length character of a field================ -->
  if(checkType == "Equal"){
    if ($('#' + fieldId).val().length != fieldLen && $('#' + fieldId).val().length > 0){
      $('#' + fieldId).focus();
      swal(fieldName + ' should be ' + fieldLen + ' digit length!!!');
      return false;
    }
    return true;
  }

}

<!-- ############### Allow only Numeric Input and replace '' when find special character on keyup/keypress/keydown################ -->
<!-- =========Call Below function by on KeyUp======= -->
function checkNum(fieldId){
  var tempVal = $("#"+fieldId).val().replace(/[^0-9\.]/g,'');
  $("#"+fieldId).val(tempVal);
}

<!-- ############### Allow only Alphabets and Numeric Input and replace '' when find special character on keyup/keypress/keydown ################ -->
<!-- =========Call Below function by on on KeyUp======= -->
function checkAlphabetAndNumber(fieldId){
  var tempVal = $("#"+fieldId).val().replace(/[^a-zA-Z0-9 ]/g,'');
  $("#"+fieldId).val(tempVal);
}

<!-- ############### Allow only AlphaNumeric character ################ -->
<!-- =========Call Below function by on button submit ======= -->
function allowAlphaNumericCharacter(fieldId,message){
  //var alphaNumericRegExp = new RegExp(/^([0-9]|[a-z])+([a-z]+)$/i);
	var alphaNumericRegExp = new RegExp("^[a-zA-Z0-9\s]+$");
  var fieldVal = $("#"+fieldId).val();
  
  if(alphaNumericRegExp.test(fieldVal) == false){
    $('#' + fieldId).focus();
    swal(message);
    return false;
  }
  return true;

}

<!-- ############### Validate Special character ################ -->
<!-- =========Call Below function by on form submit or any event======= -->
function checkSpecialCharacter(fieldId,message){
  var SpecialCharRegExp = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/;
  var fieldVal = $("#"+fieldId).val();
  
  if(SpecialCharRegExp.test(fieldVal)){
    $('#' + fieldId).focus();
    swal(message);
    return false;
  }
  return true;

}

<!-- ############### Function to check custom special character  ############### -->
<!-- ============== custom Special character is input ============= -->
function checkCustomSpecialChar(fieldId,fieldName,splCharacterArr){
    var str = $('#' + fieldId).val();
    
    for (var i = 0; i < splCharacterArr.length; i++){
        if (str.indexOf(splCharacterArr[i]) > 0) {
            $('#' + fieldId).focus();
            swal("Special character " + splCharacterArr[i] + " is not allowed For "+ fieldName +"!!!");
            return false;
        }
    }
    return true;
}

<!-- #################### first character not to be special character #####################-->
function checkSpecialCharacterFirst(fieldId,message){
    var SpecialCharRegExp = /[ !@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/;
    var strInputval = $('#' + fieldId).val();
    var firstChar = strInputval.charAt(0);

    if(SpecialCharRegExp.test(firstChar)){
      $('#' + fieldId).focus();
      swal(message);
      return false;
    }
    return true;

}

<!-- #################### first character not to be special character Custom input of regExp #####################-->
function checkSpecialCharacterFirstCustom(fieldId,message,SpecialCharRegExp){
    //var SpecialCharRegExp = /[ !@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/;
    var strInputval = $('#' + fieldId).val();
    var firstChar = strInputval.charAt(0);

    if(SpecialCharRegExp.test(firstChar)){
      $('#' + fieldId).focus();
      swal(message);
      return false;
    }
    return true;

}

<!-- ################ Function to check email for validation ############## -->
function checkEmailId(fieldId,message){
    //var pattern = new RegExp(/^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/);
    var emailPattern = /^[A-Za-z0-9\-_\.]+@+[A-Za-z0-9\-\.]+\.+[A-Za-z]{2,10}$/;
    var email = $('#' + fieldId).val();
    if (email != ''){
        if (emailPattern.test(email) == true)
            return true;
        else{
            $('#' + fieldId).focus();
            swal(message);
            return false;
        }
    }else
      return true;
        
}


<!-- ############## Function to check Valid indian Mobile number with digit 0-9 and length=10 ############# -->
<!-- ex:  9861098610 -->
function checkMobileNumberIN(fieldId,message) {
    var mobile = $('#' + fieldId).val();
    var mobileReg = /^\d{10}$/;
    /* /^[0][1-9]\d{9}$|^[1-9]\d{9}$/g; */

    if (mobile != ''){
        if (mobileReg.test(mobile)){
            return true;
        }else {
            $('#' + fieldId).focus();
            swal(message);
            return false;
        }
    }else
        return true;
}

<!-- ############## Function to check Valid indian Mobile number with digit 0-9, length=10, and countrycode ############# -->
<!-- ex:  +91 9861098610 or +919861098610 -->
function checkMobileNumberINCode(fieldId,message) {
    var mobile = $('#' + fieldId).val();
    var mobileReg = /^(\+\d{1,3}[-])\d{10}$/;

    if (mobile != ''){
        if (mobileReg.test(mobile)){
            return true;
        }else {
            $('#' + fieldId).focus();
            swal(message);
            return false;
        }
    }else
        return true;
}
<!-- ############## Validate the telephone number ############## -->
<!-- example:+91-674-2495452  -->
function validateTelephone(fieldId, message) {
    var telephonVal = $('#'+fieldId).val();
    var telephoneReg = /^([1-9]{1}[0-9]{0,1}[0-9]{0,1}[\-]{1}[1-9]{1}[0-9]{1,5}[\-]{1})[1-9]{1}[0-9]{5,6}$/;
    //var telephoneReg = /^((\+[1-9]{1,4}[ \-]*)|(\([0-9]{2,3}\)[ \-]*)|([0-9]{2,4})[ \-]*)*?[0-9]{3,4}?[ \-]*[0-9]{3,4}?$/;
    //alert((telephoneReg.test(telephonVal)));
    if (telephonVal != ''){
        if (telephoneReg.test(telephonVal))
            return true;
        else {
            $('#' + fieldId).focus();
            swal(message);
            return false;
        }
    }else
        return true;
}


<!-- ########## BANK IFSC CODE VALIDATION -->
function AllowIFSC(id) {
    var ifsc = document.getElementById(id).value;
    var reg = /[A-Z|a-z]{4}[0][a-zA-Z0-9]{6}$/;

    if (ifsc.match(reg)) {
        return true;
    }
    else {
        swal({
        	title	:	"You have entered wrong IFSC Code",
        	text	:	"Please enter again...!!!",
        	type	:	"warning",
        })
        
        $("#"+id).val("");
    }
}

<!-- ########## GST VALIDATION -->
function gstValid(id) {
	var inputvalues = $("#"+id).val();    
    var gstinformat = new RegExp('^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$');    
    if (gstinformat.test(inputvalues)) {    
        return true;    
    } else {    
        swal({
        	title	:	"Please Enter Valid GSTIN Number",
        	type	:	"warning",
        }) 
        $("#"+id).val('');    
    }    
}
	function dateValidation(fromDateId, toDateId, name1, name2) {
		
		var fromDate = $("#"+fromDateId).val();		
		var toDate = $("#"+toDateId).val();
		var date1 = fromDate.split("-");
		var date2 = toDate.split("-");
		var newFromDate = date1[2]+date1[1]+date1[0];
		var newToDate = date2[2]+date2[1]+date2[0];
	
		if(newToDate < newFromDate) {
		swal({
		title : name1+" can't be greater than "+name2 +" !!!" ,
		type : "warning",
		})
		$("#"+toDateId).val("");
		}
		}
        
	//function to check file format
	function checkImageFormat(sender, size) {
	    var fileSize = $('#' + sender.id)[0].files[0].size / 1024 / 1024;
	    var validExts = new Array(".JPG", ".PNG", ".PDF",".XLS",".DOCX",".XLSX");
	    var fileExt = sender.value;
	    fileExt = fileExt.substring(fileExt.lastIndexOf('.'));
	    if (validExts.indexOf(fileExt.toUpperCase()) < 0) {
	        swal("Invalid file selected, valid files are of " + validExts.toString() + " types");
	        $('#' + sender.id).val("");
	        return false;
	    } else if (fileSize > size) {
	        $('#' + sender.id).val("");
	        swal("File size exceeds" + size + " MB");
	    } else
	        return true;
	}
	//function to check file format
	function checkImageFormatOnly(sender) {
	    var fileSize = $('#' + sender.id)[0].files[0].size / 1024 / 1024;
	    var validExts = new Array(".JPG", ".PNG", ".JPEG");
	    var fileExt = sender.value;
	    fileExt = fileExt.substring(fileExt.lastIndexOf('.'));
	    if (validExts.indexOf(fileExt.toUpperCase()) < 0) {
	        swal("Invalid file selected, valid files are of " + validExts.toString() + " types");
	        $('#' + sender.id).val("");
	        return false;
	    } else if (fileSize > 5) {
	        $('#' + sender.id).val("");
	        swal("File size exceeds 5 MB");
	    } else
	        return true;
	}
	//function to check file format
	function checkFileFormat(sender) {
	    var fileSize = $('#' + sender.id)[0].files[0].size / 1024 / 1024;
	    var validExts = new Array(".PDF");
	    var fileExt = sender.value;
	    fileExt = fileExt.substring(fileExt.lastIndexOf('.'));
	    if (validExts.indexOf(fileExt.toUpperCase()) < 0) {
	        swal("Invalid file selected, valid files are of " + validExts.toString() + " types");
	        $('#' + sender.id).val("");
	        return false;
	    } else if (fileSize > 20) {
	        $('#' + sender.id).val("");
	        swal("File size exceeds 10 MB");
	    } else
	        return true;
	}
/*	<input type="file" class="custom-file-input" name="UploadFile" id="UploadFile" onChange="checkImageFormat(this,'{{$userSettingsArray->TULS_Max_FileSize}}')">*/

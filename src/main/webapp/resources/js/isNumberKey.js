function isNumberKey(evt) {
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (evt.ctrlKey === true) {
        return (charCode > 37 && (charCode < 48 || charCode > 57 ));
    }
    return !(charCode > 39 && (charCode < 48 || charCode > 57));
   
}



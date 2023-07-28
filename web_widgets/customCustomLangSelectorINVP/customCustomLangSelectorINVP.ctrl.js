function PbInputCtrl($scope, $log, widgetNameFactory) {
    
    $scope.selectedLang = "";
    $scope.updateLang = function(){
        document.cookie="BOS_Locale=" + $scope.selectedLang + ";path=/;max-age=31536000";
		try {
			var url = new URL(document.location.href);
			url.searchParams.delete('locale');
			url.searchParams.delete('_l');
			document.location.assign(url.href);
		} catch (error) {
			document.location.reload();
		}
    }   
    
    function getCookie() {
        var cookieName = "BOS_Locale";
        var cookies = document.cookie.split(';');
    
        for (var i = 0; i < cookies.length; i++) {
            var cookie = cookies[i].trim();
            if (cookie.indexOf(cookieName) == 0) {
                // return cookie.substring(cookieName.length + 1, cookie.length);
                $scope.selectedLang = cookie.substring(cookieName.length + 1, cookie.length);
                break;
            }
        }
    
        if($scope.selectedLang === ""){
            $scope.selectedLang = "es"
        }
    }
    
    getCookie();
}
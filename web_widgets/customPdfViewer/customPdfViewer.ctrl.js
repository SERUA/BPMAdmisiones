function ($scope, $sce) {
    debugger;
    $scope.pdfUrl = "";
     $scope.$watch(function(){
        return $scope.properties.contentStorageId;
    }, function (newValue, oldValue){
        debugger;
        $scope.pdfUrl =$sce.trustAsResourceUrl($scope.properties.contentStorageId);
        if($scope.pdfUrl === ""){
            $scope.properties.oculto = "true";
        }
    });
}
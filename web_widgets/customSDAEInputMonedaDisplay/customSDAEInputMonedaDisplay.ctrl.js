function PbInputCtrl($scope, $log, widgetNameFactory) {

    var blockVar;
 
    this.name = widgetNameFactory.getName('pbInput');
    $scope.value = "";

    if (!$scope.properties.isBound('value')) {
        $log.error('the pbInput property named "value" need to be bound to a variable');
    }
    
    $scope.$watch("properties.value",  function(){
       if($scope.properties.value || $scope.properties.value == 0){
            // let parser = Intl.NumberFormat("en-US", {
            //     style: "currency",
            //     currency: "USD",
            //     useGrouping: true,
            // }); 
            let parser = Intl.NumberFormat('en-US');
            
            $scope.value = parser.format($scope.properties.value);
       } 
    });
    
}
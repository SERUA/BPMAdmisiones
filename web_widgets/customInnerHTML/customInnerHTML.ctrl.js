function PbSelectCtrl($scope, $parse, $log, widgetNameFactory, $timeout, $window, $element) {
    var sigleTime=true;
$scope.$watch('properties.text', function(value) {
    debugger;
    if (angular.isDefined(value) && value !== null) {
        setTimeout(function(){ var element = document.getElementById($scope.properties.divid);
        if(element.innerHTML == value){
            
        }else{
        element.innerHTML = value
            if(sigleTime && $scope.properties.goBottom){
            
            setTimeout(function(){ window.scrollTo(0,document.body.scrollHeight); 
                window.onscroll = function(ev) {
                if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
                    sigleTime=false;
                }
            };
            },   1000);
            
            }
        
        }
        }, 100);
      
    }
  });
    var hidden = document.getElementsByClassName("oculto");
    for(var i=0; i<hidden.length; i++){
        hidden[i].classList.add("invisible")
    }


  
}
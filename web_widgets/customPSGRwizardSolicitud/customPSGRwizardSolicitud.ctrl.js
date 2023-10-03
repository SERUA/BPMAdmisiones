function ($scope) {
    $scope.maxCompletedStep = $scope.properties.selectedIndex;
    
    $scope.nextStep = function(){
        if($scope.properties.content[$scope.properties.selectedIndex].isValid){
            if($scope.properties.selectedIndex < $scope.properties.content.length -1){
                $scope.properties.selectedIndex = $scope.properties.selectedIndex + 1;
                $scope.setSelected($scope.properties.selectedIndex);
            }
        }
    }
    
    $scope.previousStep = function(){
        if($scope.properties.selectedIndex > 0){
            $scope.properties.selectedIndex = $scope.properties.selectedIndex - 1;
            $scope.setSelected($scope.properties.selectedIndex);
        }
    }
    
    $scope.scrollWizard = function(_direction){
        $("#wizard-container")
        element.scrollTop = pixels;
    };
    
    $scope.scrollTo = function scrollToCategory(_direction) {
        var elem = document.getElementById("wizard-container");
        var elemWidth = document.querySelector(".wizard-step-container").scrollWidth;
        if (_direction === "back") {
          if (elem.scrollLeft === 0) {
            elem.scrollTo({
              left: elem.scrollWidth,
              behavior: 'smooth',
            });
          } else {
            elem.scrollTo({
              left: elem.scrollLeft - elemWidth,
              behavior: 'smooth',
            });
          }
        } else if (_direction === "forward") {
          if (elem.scrollWidth - elem.scrollLeft === elem.clientWidth) {
            elem.scrollTo({
              left: 0,
              behavior: 'smooth',
            });
          } else {
            elem.scrollTo({
              left: elem.scrollLeft + elemWidth,
              behavior: 'smooth',
            });
          }
        }
      }

    $scope.setSelected = function(_index){
        let isStepForward = _index ===  ($scope.maxCompletedStep + 1) || (_index === ($scope.properties.currentWindow - 1));
        let isStepBAckward = _index === ($scope.properties.currentWindow - 1);
        if( isStepBAckward || isStepForward || $scope.maxCompletedStep >= _index){
            for(let i = 0; i < $scope.properties.content.length; i++){
                if(_index === i){
                    if(isStepForward){
                       $scope.maxCompletedStep = $scope.maxCompletedStep < _index ? _index : $scope.maxCompletedStep; 
                    }

                    getElementOnScreen("wizard_step_" + _index);
                    
                } 
            }
        } 
    }
    
    $scope.selectStepManually = function(_index){
        if(_index <= $scope.maxCompletedStep){
            if(_index!== 0){
                $scope.properties.selectedIndex = _index;
            }
        }
    }
    
    function getElementOnScreen( _id){
        //let element = document.getElementById(_id);
        //element.scrollIntoView();
        document.getElementById(_id).scrollIntoView({block: "end", behavior: "smooth"});
    }
    
    $scope.$watchCollection("properties.content", function(newValue, oldValue) {
        if($scope.properties.content !== undefined){
            // $scope.setSelected(0);
        }
    });

    $scope.$watchCollection("properties.selectedIndex", function(newValue, oldValue) {
        if($scope.properties.selectedIndex !== undefined){
            $scope.maxCompletedStep = $scope.properties.selectedIndex;
            $scope.setSelected($scope.properties.selectedIndex);
        }
    });
}
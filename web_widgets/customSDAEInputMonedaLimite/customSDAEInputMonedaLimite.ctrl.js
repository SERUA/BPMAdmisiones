function PbInputCtrl($scope, $log, widgetNameFactory) {

  'use strict';

  this.name = widgetNameFactory.getName('pbInput');

  if (!$scope.properties.isBound('value')) {
    $log.error('the pbInput property named "value" need to be bound to a variable');
  }
  
//   var currencyMask = IMask(document.getElementById('currency-mask'),
//   {
//     mask: '$num',
//     blocks: {
//       num: {
//         // nested masks are available!
//         mask: Number,
//         thousandsSeparator: ' '
//       }
//     }
//   });
}

angular.module('secureRestSample', []).
    config(function ($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(false);

        $routeProvider
            .when('/', { templateUrl: '/templates/home.html'})
            .when('/home', { templateUrl: '/templates/home.html'})
            .when('/restricted', { templateUrl: '/templates/restricted.html', controller: HomeCtrl })
            .when('/login', { templateUrl: '/templates/login.html', controller: RestrictedCtrl })
        .when('/play', { templateUrl: '/templates/play.html', controller: PlayCtrl });
    })

function RouteCtrl($scope, $http, $location) {
    $scope.isAuthenticated = false;
    $scope.user = undefined;

    function setAuthenticationStatus(isAuthenticated, user) {
        $scope.isAuthenticated = isAuthenticated;
        $scope.user = user;
    }

    function checkUserAuthentication() {
        $http.get("/api/authentication/status")
            .success(function (data) {
                setAuthenticationStatus(data.authenticated, data.user);
            })
            .error(function () {
                setAuthenticationStatus(false, undefined);
            });
    }

    checkUserAuthentication();
}


function HomeCtrl($scope, $http) {
    $scope.developers = undefined;

    $http.get("/api/sample/developers")
        .success(function (data, status) {
            $scope.developers = data;
        })
}
function RestrictedCtrl($scope, $http, $location) {
    $scope.greet = undefined;
    $scope.secretMessage = undefined;

    $http.get("/api/sample/greet")
        .success(function (data) {
            $scope.greet = data;
        })
        .error(function (data, status) {
            if(status == 403) $location.path("/login")
        });
    $http.get("/api/sample/secret")
        .success(function (data, status) {
            console.log(status);
            if(status == 200) $scope.secretMessage = data;
        })
}



function PlayCtrl($scope, $http, $timeout) {
    $scope.noGame = true;
    $scope.hasGame = false;
    $scope.waitingForPlayer=false;
    $scope.gameUri = null;
    $scope.needToDeal = false;
    $scope.youWon = false;
    $scope.youLost = false;
    
    
    
    $scope.getGameInfo = function() {
    	$http.get($scope.gameUri)
        .success(function (data, status, headers) {
        	alert(data);
        })
        .error(function (data, status) {
            alert(data);
        });
    };
    
    var self = this;
    var startPolling = function(){
      function poll(){
    	  $http.get($scope.gameUri)
          .success(function (data, status, headers) {
        	  var me = data.me;
        	  if (data.details.player2Id == null) {
        		  $scope.waitingForPlayer = true;
        		  $scope.joinUri = data.join;
        	  } else {
        		  $scope.waitingForPlayer = false;
        		  if (data.details.winner != null) {
        			  $scope.needToDeal = false;
        			  if (data.details.winner == me) {
        				  $scope.youWon = true;
        			  } else {
        				  $scope.youLost = true;
        			  }
        		  } else {
                	  if (me == data.details.player1Id) {
                		  
                		  if (!data.details.player1Dealt) {
                			  $scope.needToDeal = true;
                		  } else {
                			  $scope.needToDeal = false;
                		  }
                	  } else {
                		  if (!data.details.player2Dealt) {
                			  $scope.needToDeal = true;
                		  } else {
                			  $scope.needToDeal = false;
                		  }
                	  }
        		  }
        	  }
        	  $timeout(poll, 1000);
          })
          .error(function (data, status) {
              alert(data);
          });
    	  
    	  
      };
      poll();
    };
    
    
    $scope.createGame = function()
    {
    	var xsrf = $.param({firstTo: 2});
        $http.post("/api/games",xsrf , {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function (data, status, headers) {
        	$scope.hasGame = true;
        	
        	$scope.gameUri = headers("Location");
            $scope.noGame = false;
            startPolling();

        })
        .error(function (data, status) {
            alert(data);
        });
    };
    
    
    $scope.deal = function(choice)
    {
       	var xsrf = $.param({choice: $scope.choice.toUpperCase()});
       	$http.post($scope.gameUri+"/choice", xsrf, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function (data, status, headers) {
//        	alert("You've dealt a blow!");
        })
        .error(function (data, status) {
            alert(data);
        });
    };
    
    $scope.join = function() {
       	$http.post($scope.joinUri, null, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function (data, status, headers) {
        	$scope.hasGame = true;
        	
        	$scope.gameUri = headers("Location");
            $scope.noGame = false;
            startPolling();
        })
        .error(function (data, status) {
            alert(data);
        });
    }
    
}

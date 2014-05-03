// Wait till the browser is ready to render the game (avoids glitches)
window.requestAnimationFrame(function () {
var size = 4;
var gameManager = new GameManager(size, KeyboardInputManager, HTMLActuator, LocalScoreManager);
  for (var x = 0; x < size; x++) {
    for (var y = 0; y < size; y++) {
      //alert(x + "," + y);
      var xy = { x: x, y: y };
      var tile = new Tile(xy, 2);
      gameManager.grid.insertTile(tile);
    }
  }
  //gameManager.actuate();
});

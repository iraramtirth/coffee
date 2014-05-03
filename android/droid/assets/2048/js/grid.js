function Grid(size) {
  this.size = size;

  this.cells = [];//里面保存的都是Tile对象

  this.build();
}

// 初始化一个二维数据.初始数据都为null
Grid.prototype.build = function () {
  for (var x = 0; x < this.size; x++) {
    var row = this.cells[x] = [];
    for (var y = 0; y < this.size; y++) {
      row.push(null);
    }
  }
};

//随机返回一个可用的值{x:x, y:y}。枚举类型
Grid.prototype.randomAvailableCell = function () {
  var cells = this.availableCells();
  //alert("randomAvailableCell " + cells);
  if (cells.length) {
    return cells[Math.floor(Math.random() * cells.length)];
  }
};
//返回所有可用的cells对象(数组), 每一个数组值为{x:x, y:y}枚举类型值.记录的是坐标
Grid.prototype.availableCells = function () {
  var cells = [];
  this.eachCell(function (x, y, tile) {
    if (!tile) {
      cells.push({ x: x, y: y });
    }
  });
  return cells;
};

// Call callback for every cell
Grid.prototype.eachCell = function (callback) {
  for (var x = 0; x < this.size; x++) {
    for (var y = 0; y < this.size; y++) {
      callback(x, y, this.cells[x][y]);
    }
  }
};

// Check if there are any cells available
Grid.prototype.cellsAvailable = function () {
  //alert("cellsAvailable" + (this.availableCells().length > 0));
  return this.availableCells().length > 0;
};

// Check if the specified cell is taken
Grid.prototype.cellAvailable = function (cell) {
  //alert("cellAvailable" + (!this.cellContent(cell)));
  return !this.cellContent(cell);
};

Grid.prototype.cellContent = function (cell) {
  if (this.withinBounds(cell)) {
    return this.cells[cell.x][cell.y];
  } else {
    return null;
  }
};

// Inserts a tile at its position
Grid.prototype.insertTile = function (tile) {
  
  this.cells[tile.x][tile.y] = tile;
};

Grid.prototype.removeTile = function (tile) {
  this.cells[tile.x][tile.y] = null;
};

Grid.prototype.withinBounds = function (position) {
  return position.x >= 0 && position.x < this.size &&
         position.y >= 0 && position.y < this.size;
};

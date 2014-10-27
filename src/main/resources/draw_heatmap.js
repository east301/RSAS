// utils
var unique = function(arr) {
  var storage = {};
  var uniqueArray = new Array();
  var i,value;
  for ( i = 0; i < arr.length; i++) {
    value = arr[i];
    if (!(value in storage)) {
      storage[value] = true;
      uniqueArray.push(value);
    }
  }
  return uniqueArray;
};

var whichIs = function(arr, str) {
  //console.log(arr);
  //console.log(str);
  var i, value;
  for ( i = 0; i < arr.length; i++) {
    value = arr[i];
    if (value === str) {
      return i;
    }
  }
  return -1;
};



// main
var HeatMap = function() {
    this._margin = { top: 120, right: 1000, bottom: 100, left: 50 };
    this._gridSize = 35;
    this._legendElementWidth = 55;
    
    this._color_step = [0, 1];
    this._color_range = ['#00ffff', '#ff00ff'];
    
    var c = d3.scale.linear()
        .domain(this._color_step)
        .range(this._color_range);
    
    this._colors = $.map(Array(2), function(v, i){
        return c(i);
    });
    
    this._colorScale = d3.scale.quantile()
        .domain([0, 1])
        .range(this._colors);
};

HeatMap.prototype.render = function(resource) {
    var self = this;
	
	var margin = this._margin;
    var gridSize = this._gridSize;
    var legendElementWidth = this._legendElementWidth;
    var colors = this._colors;
    var colorScale = this._colorScale;

    var rowAsArray = new Array();
    var colAsArray = new Array();
    for(var i = 0; i < resource.length; i++) {
        rowAsArray[i] = resource[i].row;
        colAsArray[i] = resource[i].col;
    }
    this._rows = unique(rowAsArray);
    this._cols = unique(colAsArray);
    //whichIs(this._cols, "9212");
    //console.log(this._rows);
    //console.log(this._cols);
    
	this._width = this._cols.length * gridSize + margin.left + margin.right;
    this._height = this._rows.length * gridSize + margin.top + margin.bottom;

    this._svg = d3.select('#chart').append('svg')
        .attr('width', this._width)
        .attr('height', this._height)
        .append('g')
        .attr('transform', 'translate(0, 0)');
    
    this._heatmap = this._svg.selectAll('.cell')
        .data(resource)
        .enter().append('rect')
        .attr('x', function(d) {return whichIs(self._cols, d.col) * gridSize;})
        .attr('y', function(d) {return whichIs(self._rows, d.row) * gridSize;})
        .attr('rx', 4)
        .attr('ry', 4)
        .attr('width', gridSize)
        .attr('height', gridSize)
        .style('stroke', '#E6E6E6')
        .style('stroke-width', '4px')
        .style('fill', function(d){
            //return ((d.has == 1) ? colorScale(d.pval) : '#ffffff')
            return ((d.has === 1) ? '#ff00ff' : '#00ffff');
        })
        .attr('class', 'cell')
        .attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');
    
    this._rowLabels = this._svg.selectAll('.rowLabel')
        .data(this._rows)
        .enter().append('text')
        .text(function (d) { return d; })
        .attr('x', this._cols.length * gridSize + margin.left)
        .attr('y', function (d, i) { return i * gridSize + margin.top; })
        .style('text-anchor', 'start')
        .style('font-size', '11pt')
        .style('font-family', 'Arial')
        .attr('transform', 'translate(' + (gridSize / 2) + ',' + (gridSize / 1.5) + ')')
        .attr('class', 'rowLabel');
    
    this._colLabels = this._svg.selectAll('.colLabel')
        .data(this._cols)
        .enter()
        .append('svg:a')
        .attr('xlink:href', function(d){ 
            var ret = 'http://coxpresdb.jp/data/gene/' + d + '.shtml';
            return ret; 
        })
        .append('text')
        .text(function (d) { return d; })
        .attr('x', function (d, i) { return i * gridSize + margin.left + gridSize/1.5; })
        .attr('y', margin.top - gridSize/2)
        .style('font-size', '11pt')
        .style('font-family', 'Arial')
        .style('fill', '#0000ff')
        .attr('transform', function(d, i){ 
            var rx = i * gridSize + margin.left + gridSize/1.5;
            var ry = margin.top - gridSize/2;
            var ret = 'rotate(' + '-70' + ', ' + rx + ', ' + ry + ')';
            return ret;
        })
        .attr('class', 'colLabel');
            
    this._legend = this._svg.selectAll('.legend')
        .data([0].concat(colorScale.quantiles()), function(d) { return d; })
        .enter().append('g')
        .attr('class', 'legend');
    
    this._legend.append('rect')
        .attr('x', function(d, i) { 
            // return legendElementWidth * i + margin.left;
            return margin.left;
        })
        .attr('y', function(d, i) { 
            return (margin.top + self._rows.length * gridSize + i * gridSize + gridSize/2);
        })
        .attr('width', legendElementWidth)
        .attr('height', gridSize / 2)
        .style('fill', function(d, i) { return colors[i]; });
    
    this._legend.append('text')
        .attr('class', 'legendLabel')
        .attr('x', function(d, i) { 
            // return legendElementWidth * i + margin.left;
            return margin.left + legendElementWidth + gridSize/2;
        })
        .attr('y', function(d, i) { 
            return (margin.top + self._rows.length * gridSize + (i + 1) * gridSize);
        })
        .text(function(d, i) { 
            if(i === 0){ return 'row name does not contain col name'; }
			else if(i === 1){ return 'row name contains col name'; } 
        })
        .style('font-size', '12.5pt')
        .style('font-family', 'Arial');
        //.text(function(d) { return '≥ ' + d; });
};

var heatmap = new HeatMap();
var drawHeatmap = function(data) { heatmap.render(data); };
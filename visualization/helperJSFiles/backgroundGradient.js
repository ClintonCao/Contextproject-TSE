
var backgroundGradient = (function() {
	var colorMethod = 0;
	
	var greenTints = [d3.rgb("#8c9b8b"), d3.rgb('#0b9c01')];	
	var greenScale = d3.scale.linear().domain([0, 100]).interpolate(d3.interpolateHcl).range(greenTints);
	var blueTints = [d3.rgb("#a7b1bc"), d3.rgb('#06387b')];	
	var blueScale = d3.scale.linear().domain([0, 100]).interpolate(d3.interpolateHcl).range(blueTints);
	var redTints = [d3.rgb("#c2b6b6"), d3.rgb('#8e0000')];	
	var redScale = d3.scale.linear().domain([0, 100]).interpolate(d3.interpolateHcl).range(redTints);
	var grayTints = [d3.rgb("#a2a2a2"), d3.rgb('#a2a2a2')];	
	var grayScale = d3.scale.linear().domain([0, 100]).interpolate(d3.interpolateHcl).range(grayTints);
	
	var twoColors = [d3.rgb("#00a700"), d3.rgb('#a90000')];
	// default color settings
	var color = d3.scale.linear().domain([0, 100]).interpolate(d3.interpolateHcl).range(twoColors);

	/*
	 *
	 * Calculates the worst case of #warnings/loc
	 *
	 */ 
	function getRelativeWarnings() {
		var worstRatio = 0;
		for(var p =0; p < inputData.length; p++){
			var package = inputData[p];
			var classesArray = package.classes;
			for (i = 0; i < classesArray.length; i++) {
				classObjectJson = classesArray[i];
				var numberOfWarnings = 0;
				for (j = 0; j < classObjectJson.warningList.length; j++) { 
					numberOfWarnings++;
		  		}
		  		var curRatio = numberOfWarnings / classObjectJson.loc;
		  		if(curRatio > worstRatio) {
		  			worstRatio = curRatio;
		  		}
			}
		}
		return (worstRatio * 100);
	}
	function colorsRelative() {
			return d3.scale.linear().domain([0, getRelativeWarnings()]).interpolate(d3.interpolateHcl).range(twoColors);
	}
	function colorsAbsolute() {
			return d3.scale.linear().domain([0, 100]).interpolate(d3.interpolateHcl).range(twoColors);
	}
	function calculateBackgroundGradient(svg,ratioArray,weight, id){
		var total = ratioArray[0] + ratioArray[1] + ratioArray[2];
			if ( total == 0 ) {
				var firstRatio = 0;
				var firstRatio1 = 0.01;
				var secondRatio = 0;
				var secondRatio1 = 0.01;
				var end = 0;
			}else{
				var firstRatio = ratioArray[0] / total * 100;
				var firstRatio1 = firstRatio + 0.01;
				var secondRatio = ( ratioArray[0] + ratioArray[1]) / total * 100;
				var secondRatio1 = secondRatio + 0.01;
				var end = 100;
			}
			var firstEdge = firstRatio + "%";
			var firstEdge1 = firstRatio1 + "%";
			var secondEdge = secondRatio + "%";
			var secondEdge1 = secondRatio1 + "%";
			var endEdge = end + "%";
           var gradient = svg.append("defs")
	  .append("linearGradient")
		.attr("id", "gradient" + id)
		.attr("x1", "0%")
		.attr("y1", "0%")
		.attr("x2", "100%")
		.attr("y2", "0%")
		.attr("spreadMethod", "pad");
	
	gradient.append("stop")
		.attr("offset", "0%")
		.attr("stop-color", greenScale(weight*100))
		.attr("stop-opacity", 1);
		
	gradient.append("stop")
		.attr("offset", firstEdge)
		.attr("stop-color",  greenScale(weight*100))
		.attr("stop-opacity", 1);
		
	gradient.append("stop")
		.attr("offset", firstEdge)
		.attr("stop-color", redScale(weight*100))
		.attr("stop-opacity", 1);
	
	gradient.append("stop")
		.attr("offset", secondEdge)
		.attr("stop-color", redScale(weight*100))
		.attr("stop-opacity", 1);
		
	gradient.append("stop")
		.attr("offset", secondEdge1)
		.attr("stop-color", blueScale(weight*100))
		.attr("stop-opacity", 1);
		
	gradient.append("stop")
		.attr("offset", endEdge)
		.attr("stop-color", blueScale(weight*100))
		.attr("stop-opacity", 1);
		return gradient;
	}
	function calculateBackground(svg, weight, relative, id){
		if(relative){
			var currentColorScale = colorsRelative();
		}else{
			var currentColorScale = colorsAbsolute();
		}
		
		var gradient = svg.append("defs")
			.append("linearGradient")
			.attr("id", "gradient" + id)
			.attr("x1", "0%")
			.attr("y1", "0%")
			.attr("x2", "100%")
			.attr("y2", "0%")
			.attr("spreadMethod", "pad");
	
		gradient.append("stop")
			.attr("offset", "0%")
			.attr("stop-color", currentColorScale(weight*100))
			.attr("stop-opacity", 1);
		return gradient;
	}
	return {

        getBackground: function(svg,ratioArray,weight, id) {
			if ( weight > 1 ){ weight = 1;}
			switch(colorMethod) {
				case 0:
					return calculateBackground(svg, weight, true, id);
					break;
				case 1:
					return calculateBackground(svg, weight, false, id);
					break;
				case 2:
					return calculateBackgroundGradient(svg,ratioArray,weight, id);
					break;
			} 
		},
		setColorMethod: function(index){
			colorMethod = index;
		}
    }
	
}());
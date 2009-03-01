<%@ 
taglib
    uri="http://glowaxes.com/taglibs/ga"
    prefix="ga"%>
  <body bgcolor="yellow">
    <ga:rasterizer width="120" height="35">
    
      <svg width="120" height="35"
           xmlns:svg="http://www.w3.org/2000/svg"
           xmlns="http://www.w3.org/2000/svg"
           xmlns:xlink="http://www.w3.org/1999/xlink">

        <defs>
          <filter id="virtual_light"
                  filterUnits="objectBoundingBox"
                  x="-0.1"
                  y="-0.1"
                  width="1.2"
                  height="1.2">

            <!-- temporary white surface -->
            <feGaussianBlur in="SourceAlpha" stdDeviation="0" result="blur" />
            <feSpecularLighting in="blur"
                                surfaceScale="15"
                                specularConstant="1"
                                specularExponent="5"
                                lighting-color="#FFFFFF"
                                result="specOut">
              <fePointLight x="-5000" y="-10000" z="20000" />
            </feSpecularLighting>
            <feComposite in="specOut"
                         in2="SourceAlpha"
                         operator="in"
                         result="specOut" />

            <feGaussianBlur in="SourceAlpha"
                            stdDeviation="5"
                            result="alpha_blur" />

            <!-- virtual shadow effect -->
            <feOffset in="alpha_blur"
                      dx="4"
                      dy="4"
                      result="offset_alpha_blur" />

            <!-- vitual lighting effect -->
            <feSpecularLighting in="alpha_blur"
                                surfaceScale="2"
                                specularConstant="1"
                                specularExponent="20"
                                lighting-color="yellow"
                                result="spec_light">
              <fePointLight x="-5000" y="-10000" z="10000" />
            </feSpecularLighting>
            <feComposite in="spec_light"
                         in2="SourceAlpha"
                         operator="in"
                         result="spec_light" />
            <feComposite in="SourceGraphic"
                         in2="spec_light"
                         operator="out"
                         result="spec_light_fill" />

            <!-- combine effects -->
            <feMerge>
              <feMergeNode in="specOut" />
              <!-- feMergeNode in="offset_alpha_blur" / -->
              <feMergeNode in="spec_light_fill" />
            </feMerge>
          </filter>
        </defs>

        <!-- button content -->
        <rect rx="10"
              ry="10"
              width="119" height="34"
              fill="red"
              stroke="green"
              filter="url(#virtual_light)" />
         <text style="font-size:14;stroke:white;fill:white;stroke-width:0.5;baseline-shift:-8.52768;" 
               x="12.0" 
               y="12.0">A piece of text</text>
      </svg>
      
    </ga:rasterizer>
  </body>
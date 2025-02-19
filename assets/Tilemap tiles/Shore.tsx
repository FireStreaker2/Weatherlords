<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.11.2" name="Shore" tilewidth="16" tileheight="16" tilecount="5" columns="5">
 <image source="../MiniWorldSprites/MiniWorldSprites/Ground/Shore.png" width="80" height="16"/>
 <tile id="3">
  <properties>
   <property name="isSolid" type="bool" value="true"/>
  </properties>
  <objectgroup draworder="index" id="2">
   <object id="1" name="CollisionBox" x="0" y="0" width="16" height="16">
    <properties>
     <property name="isSolid" type="bool" value="true"/>
    </properties>
   </object>
  </objectgroup>
 </tile>
</tileset>

function sptransform(horm,face,ori)
  local thorm ={}
	local transdiction = getTransDictionary();
  --for k,v in pairs(transdiction[1][1][1]) do print(k,v) end
  if (face == 1) then --If message comming from male face two numbers are expected in ori
    dadface = math.floor(ori/10)
    myori = ori - (dadface*10)
    print(dadface)
    print(myori)
    local entry = transdiction[face][dadface][myori]
    --for k,v in pairs(entry) do print(k,v) end
    for i=1,#entry do
      thorm[i] = horm[entry[i]]
      --thorm = {horm[entry[1]],horm[entry[2]],horm[entry[3]],horm[entry[4]],horm[entry[5]],horm[entry[6]]}
    end
    
  else --If message comes from female face one number is expected in ori
    print(ori)
    local entry = transdiction[face][ori]
    for i=1,#entry do
      thorm[i] = horm[entry[i]]
      --thorm = {horm[entry[1]],horm[entry[2]],horm[entry[3]],horm[entry[4]],horm[entry[5]],horm[entry[6]]}
    end
  end
  --for k,v in pairs(thorm) do print(k,v) end
  
  return thorm

end


function getTransDictionary()
	local dictionary ={}
	local f1 = {}
	local f2 = {{1,3,4,2,5,6,7},{1,2,3,4,5,6,7},{1,2,3,4,5,6,7},{1,2,3,4,5,6,7}}
	local f3 = {{1,2,3,4,5,6,7},{1,2,3,4,5,6,7},{1,2,3,4,5,6,7},{1,2,3,4,5,6,7}}
	local f4 = {{1,2,3,4,5,6,7},{1,2,3,4,5,6,7},{1,2,3,4,5,6,7},{1,2,3,4,5,6,7}}

	local f11 = {{1,3,2,4,5,6,7},{1,2,3,4,7,6,5},{1,2,3,4,5,6,7},{1,2,3,4,5,6,7}}
	local f12 = {{1,2,3,4,5,6,7},{1,2,3,4,5,6,7},{1,2,3,4,5,6,7},{1,2,3,4,5,6,7}}
	local f13 = {{1,2,3,4,5,6,7},{1,2,3,4,5,6,7},{1,2,3,4,5,6,7},{1,2,3,4,5,6,7}}
	f1[1]=f11
	f1[2]=f12
	f1[3]=f13

	dictionary[1] = f1
	dictionary[2] = f2
	dictionary[3] = f3
	dictionary[4] = f4

	return dictionary
end
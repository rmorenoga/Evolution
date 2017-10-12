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
	local f2 = {{1,2,3,4,5,6},{1,2,6,5,3,4},{1,2,4,3,6,5},{1,2,5,6,4,3}}
	local f3 = {{3,4,2,1,5,6},{6,5,2,1,3,4},{4,3,2,1,6,5},{5,6,2,1,4,3}}
	local f4 = {{4,3,1,2,5,6},{5,6,1,2,3,4},{3,4,1,2,6,5},{6,5,1,2,4,3}}

	local f11 = {{1,2,3,4,5,6},{1,2,5,6,4,3},{1,2,4,3,6,5},{1,2,6,5,3,4}}
	local f12 = {{4,3,1,2,5,6},{4,3,5,6,2,1},{4,3,2,1,6,5},{4,3,6,5,1,2}}
	local f13 = {{3,4,2,1,5,6},{3,4,5,6,1,2},{3,4,1,2,6,5},{3,4,6,5,2,1}}
	f1[1]=f11
	f1[2]=f12
	f1[3]=f13

	dictionary[1] = f1
	dictionary[2] = f2
	dictionary[3] = f3
	dictionary[4] = f4

	return dictionary
end

function hsptransform(horm,face,ori,model)
  local thorm = {}
  if(model == 1) then
    local horms = {}
    for i = 1,6 do 
      horms[i] = horm[i+1]
    end
    thorm  = sptransform(horms,face,ori)
  end
  return thorm
end


function orientation(sensorO)
	local ori = -1
	local sensorOm ={}
	local index = 0
	local sign = 0

	for i=1,#sensorO do
		sensorOm[i] = sensorO[i]*100
	end

	index,sign = findmax(sensorOm)

	if (index == 3 and sign == -1) then
		ori = 1
		elseif (index == 2 and sign == 1) then
			ori = 2
			elseif (index == 3 and sign == 1) then
				ori = 3
				elseif (index == 2 and sign == -1) then
					ori = 4
					elseif (index == 1 and sign ==-1) then
						ori = 5
						elseif (index == 1 and sign == 1) then
							ori = 6
						end

	return ori

end



function findmax(vector)
	local maxitem = -1
	local index = 0
	local vectorabs = {}
	local vectorsign = {}

	for i=1,#vector do
		vectorabs[i] = math.abs(vector[i])
	end

	for i=1,#vector do
		vectorsign[i] = vector[i]/vectorabs[i]
	end

	for i=1,#vectorabs do
		if (vectorabs[i]>maxitem) then
			maxitem = vectorabs[i]
			index = i
		end
	end

	maxitem = vectorabs[index]*vectorsign[index]

	sign = vectorsign[index]

	return index,sign,maxitem

end

function groundsen(ori)
	local ground = 0
	if (ori == 1) then
		ground = 0
		elseif (ori == 2) then
			ground = 4
			elseif (ori == 3) then
				ground = 0
				elseif (ori == 4) then
					ground = 3
				elseif (ori == 5) then
					ground = 1
					elseif (ori == 6) then
						ground = 2
					end

	return ground
end



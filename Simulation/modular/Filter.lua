
oriwindow  = 12--15
oricount = {0,0,0,0,0,0}
orifiltered = 1

hormsumwindow = 7--8
hormsumfiltered = {}
hormsumarrived = {}


function filterori(ori,simstep)

	if (simstep==0) then
		orifiltered = ori
	end

	if (ori~=-1)then
		oricount[ori] = oricount[ori] + 1
	end

	if (simstep%oriwindow == 0) then
		local index,maxcount = findmaxcount(oricount) 
		orifiltered = index
		oricount = {0,0,0,0,0,0}
	end

	return orifiltered

end

function findmaxcount(count)
  
  local maxcount = 0 
  local index = -1
  
  for i=1,#count do
    if(count[i]>maxcount) then
      index = i
      maxcount = count[i]
    end
  end
    
  return index,maxcount
end

function filterhorm(hormsum,simstep)

	if (simstep==0) then
		hormsumfiltered = hormsum
	end

	table.insert(hormsumarrived,hormsum)

	if(simstep%hormsumwindow == 0) then
		local hormcount = #hormsumarrived

		local hormsumtotal = {}
		for i=1,#hormsum do
			hormsumtotal[i] = 0
		end

		for i=1,#hormsumarrived do
			hormsumtotal  = sumhormones(hormsumtotal,hormsumarrived[i])
		end

		for i=1,#hormsumtotal do
			hormsumfiltered[i] = hormsumtotal[i]/hormcount
		end

		hormsumarrived = {}
	end

	return hormsumfiltered

end

function sumhormones(hormone1,hormone2)
	--Assumes both hormones have the same size
	local hormtotal = {}
	for i=1,#hormone1 do
		hormtotal[i] = hormone1[i]+hormone2[i]
	end

	return hormtotal

end	





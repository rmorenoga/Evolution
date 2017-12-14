
oriwindow  = 10
oricount = {0,0,0,0,0,0}
orifiltered = 1



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